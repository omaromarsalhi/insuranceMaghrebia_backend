import uuid
from typing import Dict
import google.generativeai as gemini
from llama_index.core.memory import ChatMemoryBuffer
from llama_index.core.chat_engine.types import ChatMessage
import asyncio


class ChatBotManager:
    def __init__(self):
        self.api_key = ""
        gemini.configure(api_key=self.api_key)
        self.model = gemini.GenerativeModel(model_name='models/gemini-2.0-flash')
        self.sessions: Dict[str, ChatMemoryBuffer] = {}  # Session ID → Memory

    def _format_history(self, session_id: str):
        """Format conversation history for a specific session"""
        return "\n".join(
            [f"{msg.role.capitalize()}: {msg.content}"
             for msg in self.sessions[session_id].get_all()]
        )

    async def chat(self,user_input: str) -> str:
        """Handle chat with session-specific memory"""
        try:
            # Get or create session memory
            if session_id not in self.sessions:
                self.sessions[session_id] = ChatMemoryBuffer.from_defaults(token_limit=4000)

            # Add user message
            self.sessions[session_id].put(ChatMessage(role="user", content=user_input))

            # Generate prompt
            history = self._format_history(session_id)
            prompt = f"Current Conversation:\n{history}"

            # Get AI response
            response = await asyncio.to_thread(
                self.model.generate_content,
                prompt
            )
            assistant_response = response.text

            # Add assistant response
            self.sessions[session_id].put(ChatMessage(role="assistant", content=assistant_response))

            return assistant_response

        except Exception as e:
            return f"AI Error: {str(e)}"

    def cleanup_session(self, session_id: str):
        """Remove session memory when done"""
        if session_id in self.sessions:
            del self.sessions[session_id]


