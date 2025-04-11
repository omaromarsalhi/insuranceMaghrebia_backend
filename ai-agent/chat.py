from fastapi import FastAPI, WebSocket, WebSocketDisconnect
from fastapi.middleware.cors import CORSMiddleware
import json
from datetime import datetime
from chatBot import ChatBot

app = FastAPI()

# Configure CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

client = ChatBot()

class ConnectionManager:
    def __init__(self):
        self.active_connections: list[WebSocket] = []

    async def connect(self, websocket: WebSocket):
        await websocket.accept()
        self.active_connections.append(websocket)

    def disconnect(self, websocket: WebSocket):
        if websocket in self.active_connections:
            self.active_connections.remove(websocket)

    async def send_personal_json(self, message: dict, websocket: WebSocket):
        try:
            await websocket.send_json(message)
        except Exception as e:
            self.disconnect(websocket)
            print(f"Error sending message: {e}")

    async def broadcast_json(self, message: dict):
        closed_connections = []
        for connection in self.active_connections:
            try:
                await connection.send_json(message)
            except Exception as e:
                closed_connections.append(connection)
                print(f"Connection closed: {e}")

        # Remove closed connections
        for conn in closed_connections:
            self.disconnect(conn)

manager = ConnectionManager()

async def talk_to_ai(user_message: str) -> dict:
    """Function to communicate with AI"""
    try:
        response = await client.chat(user_message)
        return {
            "type": "ai",
            "content": response,
            "timestamp": datetime.now().isoformat()
        }
    except Exception as e:
        return {
            "type": "error",
            "content": f"AI Error: {str(e)}",
            "timestamp": datetime.now().isoformat()
        }

@app.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    await manager.connect(websocket)
    try:
        while True:
            try:
                data = await websocket.receive_json()
                user_message = data.get("content", "")

                # Send acknowledgement
                await manager.send_personal_json({
                    "type": "user",
                    "content": user_message,
                    "timestamp": datetime.now().isoformat()
                }, websocket)

                # Get AI response
                ai_response = await talk_to_ai(user_message)

                # Send AI response
                await manager.send_personal_json(ai_response, websocket)

            except json.JSONDecodeError:
                await manager.send_personal_json({
                    "type": "error",
                    "content": "Invalid JSON format",
                    "timestamp": datetime.now().isoformat()
                }, websocket)

    except WebSocketDisconnect:
        manager.disconnect(websocket)
        try:
            await manager.broadcast_json({
                "type": "system",
                "content": "A client left the chat",
                "timestamp": datetime.now().isoformat()
            })
        except Exception as broadcast_error:
            print(f"Broadcast error: {broadcast_error}")