from fastApi.retriever_agent.Tools import execute
from fastApi.orchestration.utils import FunctionToolWithContext
from fastApi.orchestration.workflow import AgentConfig


class RetrieverAgent(AgentConfig):
    def __init__(self):
        name = "SQL Execution Agent"
        description = "Securely executes validated SQL queries against the database"

        data_agent_prompt = """
        **SQL Query Executor Protocol**
        
        ### Role:
        You are a secure SQL execution gateway. Your ONLY function is to safely execute 
        pre-validated SQL queries using the `execute` tool.

        """

        tools = [
            FunctionToolWithContext.from_defaults(async_fn=execute),
        ]

        super().__init__(
            name=name,
            description=description,
            system_prompt=data_agent_prompt,
            tools=tools
        )

