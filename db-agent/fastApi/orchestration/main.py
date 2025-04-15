import asyncio
import configparser
import os

from llama_index.core.memory import ChatMemoryBuffer

from MyMistralAI import MyMistralAI
from fastApi.sql_agent.SQLAgent import SQLAgent
from workflow import (
    ProgressEvent,
    ToolRequestEvent,
    ToolApprovedEvent, OrchestratorAgent,
)

config = configparser.ConfigParser()
config.read("../config.ini")
os.environ["MISTRAL_API_KEY"] = config.get('API', 'mistral_key')


def get_initial_state() -> dict:
    return {
        "username": "omar salhi",
    }


async def main():
    """Main function to run the workflow."""

    from colorama import Fore, Style

    # llm = OpenAI(model="gpt-4o", temperature=0.4)
    llm = MyMistralAI()
    memory = ChatMemoryBuffer.from_defaults(llm=llm)
    initial_state = get_initial_state()
    agent_configs = [SQLAgent()]
    workflow = OrchestratorAgent(timeout=None)
    # draw a diagram of the workflow
    # draw_all_possible_flows(workflow, filename="workflow.html")

    handler = workflow.run(
        user_msg="Hello!",
        agent_configs=agent_configs,
        llm=llm,
        chat_history=[],
        initial_state=initial_state,
    )

    while True:
        async for event in handler.stream_events():
            if isinstance(event, ProgressEvent):
                print(Fore.GREEN + f"SYSTEM >> {event.msg}" + Style.RESET_ALL)

        result = await handler
        print(Fore.BLUE + f"AGENT >> {result['response']}" + Style.RESET_ALL)

        # update the memory with only the new chat history
        for i, msg in enumerate(result["chat_history"]):
            if i >= len(memory.get()):
                memory.put(msg)

        user_msg = input("USER >> ")
        if user_msg.strip().lower() in ["exit", "quit", "bye"]:
            break

        # pass in the existing context and continue the conversation
        handler = workflow.run(
            ctx=handler.ctx,
            user_msg=user_msg,
            agent_configs=agent_configs,
            llm=llm,
            chat_history=memory.get(),
            initial_state=initial_state,
        )


if __name__ == "__main__":
    asyncio.run(main())
