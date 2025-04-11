import os
import google.generativeai as gemini
from llama_index.core.memory import ChatMemoryBuffer
from llama_index.core.llms import ChatMessage
from dotenv import load_dotenv

from prompts import form_generate_prompt


# Load environment variables

class ChatBot:
    def __init__(self):
        # Configure Gemini
        self.api_key = ""
        gemini.configure(api_key=self.api_key)
        self.model = gemini.GenerativeModel(model_name='models/gemini-2.0-flash')

        # Initialize memory with LlamaIndex
        self.memory = ChatMemoryBuffer.from_defaults(token_limit=4000)

    def _format_history(self):
        """Format conversation history for Gemini"""
        return "\n".join(
            [f"{msg.role.capitalize()}: {msg.content}"
             for msg in self.memory.get_all()]
        )

    def chat(self, user_input):
        # Add user message to memory
        self.memory.put(ChatMessage(role="user", content=user_input))

        # Generate prompt with conversation history
        history = self._format_history()
        prompt = f"""
        Current Conversation:
        {history}
        """
        # Get response from Gemini
        response = self.model.generate_content(prompt)
        assistant_response = response.text

        # Add assistant response to memory
        self.memory.put(ChatMessage(role="assistant", content=assistant_response))

        return assistant_response

chatBot = ChatBot()


# if __name__ == "__main__":
#     agent = ChatBot()
#     print("Agent: Hello! How can I assist you today? (Type 'exit' to end)")
#
#     while True:
#         user_input = input("You: ")
#         if user_input.lower() in ['exit', 'quit', 'bye']:
#             print("Agent: Goodbye!")
#             break
#
#         response = agent.chat(user_input)
#         print(f"Agent: {response}")

