import json
import asyncio
from fastapi import FastAPI, WebSocket, WebSocketDisconnect
import uvicorn
from HumanoidAssistant import HumanoidAssistant  

app = FastAPI()
assistant = HumanoidAssistant()

@app.websocket("/ws/assistant")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    await websocket.send_text(json.dumps({
        "response": "Hello! I'm your assistant. Let me know if you need help",
        "success": True
    }))

    async def ws_input_handler(prompt: str) -> str:
        await websocket.send_text(json.dumps({"prompt": prompt}))
        response = await websocket.receive_text()
        data = json.loads(response)
    
        return data.get("user_input")

    try:
        while True:
            data = await websocket.receive_text()
            payload = json.loads(data)
            user_input = payload.get("user_input", "")
            
            action_data = assistant._extract_action(user_input)
            if "error" in action_data:
                await websocket.send_text(json.dumps({"error": action_data["error"]}))
                continue
            validated_data = await assistant._validate_params_async(action_data, ws_input_handler)
            response_text, success = assistant._execute_action(
                validated_data["intent"],
                validated_data["parameters"]
            )
            try:
                response_json = json.loads(response_text)
                if isinstance(response_json, dict) and response_json.get("type") == "redirect":
                    await websocket.send_text(json.dumps(response_json))
                else:
                    await websocket.send_text(json.dumps({"response": response_text, "success": success}))
            except json.JSONDecodeError:
                await websocket.send_text(json.dumps({"response": response_text, "success": success}))
            # await websocket.send_text(json.dumps({"response": response_text, "success": success}))
    except WebSocketDisconnect:
        print("Client déconnecté")

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
