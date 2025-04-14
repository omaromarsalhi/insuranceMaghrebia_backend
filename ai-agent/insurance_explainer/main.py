import uvicorn
from fastapi import FastAPI, WebSocket, WebSocketDisconnect
from fastapi.middleware.cors import CORSMiddleware
import json
import uuid
import requests

from auto_variables import vin_list, weather_list
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
        print(response)

        if response.startswith("```json"):
            cleaned_response = response.replace("```json", "").replace("```", "").strip()
            parsed_json = json.loads(cleaned_response)
        return {
            "type": "ai",
            "content": parsed_json,
        }
    except Exception as e:
        return {
            "type": "error",
            "content": f"AI Error: {str(e)}",
        }


def fetch_vin_data(vin: str):
    try:
        url = f"http://localhost:8888/api/v1/vin/decode/{vin}"
        response = requests.get(url)
        response.raise_for_status()
        data = response.json()
        return data
    except Exception as e:
        print(e)


def fetch_geoweather_data(address_info: dict):
    try:
        url = "http://localhost:8888/api/v1/geoweather"
        headers = {'Content-Type': 'application/json'}
        response = requests.post(url, json=address_info, headers=headers)
        response.raise_for_status()
        return response.json()
    except Exception as e:
        print(e)


def get_safety_features(vin_data):
    return {
        'abs': vin_data['abs'],
        'tractionControl': vin_data['tractionControl'],
        'electronicStabilityControl': vin_data['electronicStabilityControl'],
        'backupCamera': vin_data['backupCamera'],
        'daytimeRunningLights': vin_data['daytimeRunningLights'],
    }

round(1.1404371584699455, 2)
def get_weather_features(geoweather_data, factor):
    if factor == weather_list[0]:
        return {'averagePrecipitation': round(geoweather_data['averagePrecipitation'],3)}
    elif factor == weather_list[1]:
        return {
            'averageMinTmp': round(geoweather_data['averageMinTmp'],3),
            'averageMaxTmp': round(geoweather_data['averageMaxTmp'],3)
        }
    elif factor == weather_list[2]:
        return {'averageWind': round(geoweather_data['averageWind'],3)}


@app.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    session_id = str(uuid.uuid4())
    await manager.connect(websocket, session_id)
    try:
        while True:
            try:
                data = await websocket.receive_json()
                user_message = data.get("content", "")
                condition = user_message.get("condition", "")
                user_message.pop("condition", None)

                if condition == 'no_preprocessing':
                    ai_response = await talk_to_ai(user_message)
                    await manager.send_personal_json(ai_response, session_id)
                else:
                    if user_message['factor'] == 'vin':

                        received_data_from_api = fetch_vin_data(user_message['value'])

                        for factor in vin_list[0:-1]:
                            ai_response = await talk_to_ai({'factor': factor, 'value': received_data_from_api[factor]})
                            await manager.send_personal_json(ai_response, session_id)

                        ai_response = await talk_to_ai(
                            {'factor': vin_list[-1], 'value': get_safety_features(received_data_from_api)})
                        await manager.send_personal_json(ai_response, session_id)

                    else:

                        received_data_from_api = fetch_geoweather_data(user_message['value'])
                        print(received_data_from_api)

                        for factor in weather_list:
                            ai_response = await talk_to_ai(
                                {'factor': factor, 'value': get_weather_features(received_data_from_api, factor)})
                            await manager.send_personal_json(ai_response, session_id)


                        value = 'It is an urban area' if received_data_from_api[
                            'isUrbanArea'] else 'No, it is not an urban area'
                        ai_response = await talk_to_ai({'factor': 'geographic_factors', 'value': value})
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
        port=9000,  # Custom port
        reload=True  # Optional: Auto-reload
    )
