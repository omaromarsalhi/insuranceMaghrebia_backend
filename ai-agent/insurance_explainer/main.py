import uvicorn
from fastapi import FastAPI, WebSocket, WebSocketDisconnect
from fastapi.middleware.cors import CORSMiddleware
import json
from datetime import datetime
import uuid

from chat_bot import ChatBotManager

app = FastAPI()


app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

client = ChatBotManager()


class ConnectionManager:
    def __init__(self):
        self.active_connections: dict[str, WebSocket] = {}

    async def connect(self, websocket: WebSocket, session_id: str):
        await websocket.accept()
        self.active_connections[session_id] = websocket
        print(f"Active connections: {len(self.active_connections)}")

    def disconnect(self, session_id: str):
        if session_id in self.active_connections:
            del self.active_connections[session_id]
            client.cleanup_session(session_id)

    async def send_personal_json(self, message: dict, session_id: str):
        if session_id in self.active_connections:
            try:
                await self.active_connections[session_id].send_json(message)
            except Exception as e:
                print(f"Error sending message: {e}")
                self.disconnect(session_id)

    async def broadcast_json(self, message: dict):
        closed_sessions = []
        for session_id, connection in self.active_connections.items():
            try:
                await connection.send_json(message)
            except Exception as e:
                closed_sessions.append(session_id)
                print(f"Connection closed: {e}")

        for session_id in closed_sessions:
            self.disconnect(session_id)


manager = ConnectionManager()


async def talk_to_ai(user_message: dict) -> dict:
    """Function to communicate with AI using session-specific memory"""
    try:
        response = await client.chat(**user_message)
        if response.startswith("```json"):
            cleaned_response = response.replace("```json", "").replace("```", "").strip()
            parsed_json = json.loads(cleaned_response)
            print(parsed_json)
        return {
            "type": "ai",
            "content": parsed_json,
        }
    except Exception as e:
        return {
            "type": "error",
            "content": f"AI Error: {str(e)}",
        }


@app.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    session_id = str(uuid.uuid4())
    await manager.connect(websocket, session_id)
    try:
        while True:
            try:
                data = await websocket.receive_json()
                user_message = data.get("content", "")

                print(user_message)

                ai_response = await talk_to_ai(user_message)

                await manager.send_personal_json(ai_response, session_id)

            except json.JSONDecodeError:
                await manager.send_personal_json({
                    "type": "error",
                    "content": "Invalid JSON format",
                }, session_id)

    except WebSocketDisconnect:
        manager.disconnect(session_id)
        try:
            await manager.broadcast_json({
                "type": "system",
                "content": "A client left the chat",
            })
        except Exception as broadcast_error:
            print(f"Broadcast error: {broadcast_error}")


        import uvicorn

if __name__ == "__main__":
    uvicorn.run(
        "main:app",
        host="0.0.0.0",  # Listen on all network interfaces
        port=9000,       # Custom port
        reload=True      # Optional: Auto-reload
    )
