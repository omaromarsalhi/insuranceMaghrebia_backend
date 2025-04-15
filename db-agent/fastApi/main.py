from tkinter.ttk import Style

import uvicorn
from contextlib import asynccontextmanager
import uuid
import configparser
import os

from colorama import Fore
from fastapi import FastAPI
from llama_index.core.memory import ChatMemoryBuffer
from starlette.middleware.cors import CORSMiddleware
from starlette.websockets import WebSocket, WebSocketDisconnect

from fastApi.orchestration.MyMistralAI import MyMistralAI
from fastApi.orchestration.workflow import OrchestratorAgent, ProgressEvent
from fastApi.sql_agent.SQLAgent import SQLAgent

# Load configuration
config = configparser.ConfigParser()
config.read("config.ini")
os.environ["MISTRAL_API_KEY"] = config.get('API', 'mistral_key')


@asynccontextmanager
async def lifespan(app: FastAPI):
    # Initialize resources
    app.state.llm = MyMistralAI()
    app.state.agent_configs = [SQLAgent()]
    app.state.sessions = {}
    yield
    # Clean up resources
    app.state.sessions.clear()


app = FastAPI(lifespan=lifespan)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


class SessionManager:
    def __init__(self):
        self.sessions = {}

    def create_session(self, session_id, initial_state):
        memory = ChatMemoryBuffer.from_defaults(llm=app.state.llm)
        self.sessions[session_id] = {
            'memory': memory,
            'handler': None,
            'state': initial_state
        }
        return session_id

    def get_session(self, session_id):
        return self.sessions.get(session_id)

    def remove_session(self, session_id):
        if session_id in self.sessions:
            del self.sessions[session_id]


session_manager = SessionManager()


def get_initial_state() -> dict:
    return {"username": "omar salhi"}


@app.post("/start")
async def start_session():
    session_id = str(uuid.uuid4())
    session_manager.create_session(session_id, get_initial_state())
    return {"session_id": session_id}


@app.websocket("/ws/{session_id}")
async def websocket_endpoint(websocket: WebSocket, session_id: str):
    await websocket.accept()
    session = session_manager.get_session(session_id)

    if not session:
        await websocket.close(code=1008, reason="Session not found")
        return

    try:
        # Initialize workflow once per session
        if 'workflow' not in session:
            session['workflow'] = OrchestratorAgent(timeout=None)
            session['handler'] = session['workflow'].run(
                user_msg='Hello!',
                agent_configs=app.state.agent_configs,
                llm=app.state.llm,
                chat_history=session['memory'].get(),
                initial_state=session['state'],
            )

        while True:
            # Process existing events first
            handler = session['handler']
            async for event in handler.stream_events():
                if isinstance(event, ProgressEvent):
                    print(Fore.GREEN + f"SYSTEM >> {event.msg}")

            # Get user input after processing events
            await websocket.send_json({"type": "ready"})
            user_msg = await websocket.receive_text()

            # Continue workflow
            session['handler'] = session['workflow'].run(
                ctx=handler.ctx,
                user_msg=user_msg,
                agent_configs=app.state.agent_configs,
                llm=app.state.llm,
                chat_history=session['memory'].get(),
                initial_state=session['state'],
            )

            # Get and send result
            result = await session['handler']
            print(Fore.BLUE + f"AGENT >> {result['response']}" )
            await websocket.send_json({
                "type": "result",
                "content": result['response']
            })
            retrieved_data = await session['handler'].ctx.get('executed_query_results')
            await websocket.send_json({
                "type": "data",
                "content": retrieved_data
            })
            await session['handler'].ctx.set('executed_query_results','')

            # Update memory
            session['memory'].put_messages(result["chat_history"][-1:])

    except WebSocketDisconnect as e:
        print(f"Client {session_id} disconnected: code={e.code}, reason={e.reason}")
    except Exception as e:
        await websocket.close(code=1011, reason=f"Error: {str(e)}")
    finally:
        session_manager.remove_session(session_id)


if __name__ == "__main__":
    uvicorn.run(
        "main:app",
        host="0.0.0.0",
        port=9200,
        reload=True
    )