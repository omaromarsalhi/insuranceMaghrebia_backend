import os
import google.generativeai as genai
from llama_index.core.memory import ChatMemoryBuffer
from llama_index.core.llms import ChatMessage
from dotenv import load_dotenv

from prompts import form_generate_prompt


# Load environment variables

class FormBuilderAgent:
    def __init__(self):
        # Configure Gemini
        self.api_key = "AIzaSyDWNt1KT5I17FXuAHmgFts-AHyj2V6bW9w"
        genai.configure(api_key=self.api_key)
        self.model = genai.GenerativeModel(model_name='models/gemini-2.0-flash')

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
        prompt = f"""{form_generate_prompt}
        
        Current Conversation:
        {history}
        """
        # Get response from Gemini
        response = self.model.generate_content(prompt)
        assistant_response = response.text

        # Add assistant response to memory
        self.memory.put(ChatMessage(role="assistant", content=assistant_response))

        return assistant_response

agent = FormBuilderAgent()
# user_input="""take this form and put it in memory:
# "fields":[{"label":"First Name 1","type":"text","order":1,"required":true,
#            "placeholder":"Enter your first name","regex":"/^[a-zA-Z0-9\\s]{1,50}$/",
#            "javaRegex":"^[a-zA-Z0-9\s]{1,50}$","regexErrorMessage":"Invalid characters",
#            "rangeStart":0,"rangeEnd":10,"selectOptions":[],"rangeValid":true}]}"""
# response = agent.chat(user_input)
# print(f"Agent: {response}")

# if __name__ == "__main__":
#     agent = FormBuilderAgent()
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



# take this form and put it in memory:
# "fields":[{"label":"First Name 1","type":"text","order":1,"required":true,
# "placeholder":"Enter your first name","regex":"/^[a-zA-Z0-9\\s]{1,50}$/",
# "javaRegex":"^[a-zA-Z0-9\\s]{1,50}$","regexErrorMessage":"Invalid characters",
# "rangeStart":0,"rangeEnd":10,"selectOptions":[],"rangeValid":true}]}

# hwo can you help me today

# from llama_index.llms.gemini import Gemini
# from llama_index.core.memory import ChatMemoryBuffer
# from llama_index.core.agent import AgentRunner
#
# from prompts import form_generate_prompt
#
# # Set your Google API key
# GOOGLE_API_KEY = ""
#
# # Initialize Gemini
# llm = Gemini(api_key=GOOGLE_API_KEY)
#
# # Initialize memory buffer to retain conversation history
# memory = ChatMemoryBuffer.from_defaults()
#
# # Create a simple agent with memory
# agent = AgentRunner.from_llm(
#     llm=llm,
#     memory=memory,
# )
#
# # Conversation loop
# print("Chat with Gemini (type 'exit' to quit)")
# while True:
#     user_input = input("You: ")
#     if user_input.lower() == "exit":
#         break
#
#     user_input =form_generate_prompt.format(user_input=user_input)
#     # Get response with preserved memory
#     response = agent.chat(user_input)
#     print(f"Assistant: {response}")
