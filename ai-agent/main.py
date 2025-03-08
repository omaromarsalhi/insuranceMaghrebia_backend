from fastapi import FastAPI, HTTPException, Request
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from pydantic import BaseModel
from uuid import uuid4

# Session management
from fastapi_sessions.backends.implementations import InMemoryBackend
from fastapi_sessions.frontends.implementations import SessionCookie, CookieParameters

# Define session data model
class SessionData(BaseModel):
    user_id: str
    conversation_history: list = []

# Session backend (in-memory for simplicity)
backend = InMemoryBackend[str, SessionData]()

# Cookie parameters
cookie_params = CookieParameters()
cookie = SessionCookie(
    cookie_name="session_cookie",
    identifier="general_verifier",
    auto_error=True,
    secret_key="your_secret_key_here",
    cookie_params=cookie_params,
)

# Initialize FastAPI app
app = FastAPI()

# Configure CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:4300"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Chat endpoint
@app.post("/chat/")
async def chat_with_agent(request: Request):
    session_id = request.cookies.get("session_cookie")
    if session_id is None:
        # Create a new session
        session_id = str(uuid4())
        session_data = SessionData(user_id=session_id)
        await backend.create(session_id, session_data)
    else:
        # Retrieve existing session
        session_data = await backend.read(session_id)
        if session_data is None:
            # Create a new session if the existing one is invalid
            session_id = str(uuid4())
            session_data = SessionData(user_id=session_id)
            await backend.create(session_id, session_data)

    # Set the cookie in the response
    response = JSONResponse(content={"message": "Chat response", "session_id": session_id})
    cookie(session_id, response)  # Correct usage of SessionCookie
    return response

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=9100)