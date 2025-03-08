from llama_index.llms.gemini import Gemini
from llama_index.core.memory import ChatMemoryBuffer
from llama_index.core.agent import AgentRunner

# Set your Google API key
GOOGLE_API_KEY = ""

# Initialize Gemini
llm = Gemini(api_key=GOOGLE_API_KEY)

# Initialize memory buffer to retain conversation history
memory = ChatMemoryBuffer.from_defaults()

# Create a simple agent with memory
agent = AgentRunner.from_llm(
    llm=llm,
    memory=memory,
)

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