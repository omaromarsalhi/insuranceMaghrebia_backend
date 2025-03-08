from llama_index.core.base.llms.types import ChatMessage
from llama_index.llms.gemini import Gemini
from llama_index.core.memory import ChatMemoryBuffer
from llama_index.core.agent import AgentRunner

from prompts import form_generate_prompt

# Set your Google API key
GOOGLE_API_KEY = " "

# Initialize Gemini
llm = Gemini(api_key=GOOGLE_API_KEY)

# Initialize memory buffer to retain conversation history
memory = ChatMemoryBuffer.from_defaults()

# Create a simple agent with memory
agent = AgentRunner.from_llm(
    llm=llm,
    memory=memory,
    verbose=True  # Shows agent's thought process
)

# agent.update_prompts({"form_generate_prompt": form_generate_prompt})

# Conversation loop
print("Chat with Gemini (type 'exit' to quit)")
while True:
    user_input = input("You: ")
    if user_input.lower() == "exit":
        break

    user_input =form_generate_prompt.format(user_input=user_input)
    print(user_input)
    # Get response with preserved memory
    response = agent.chat(user_input)
    print(f"Assistant: {response}")