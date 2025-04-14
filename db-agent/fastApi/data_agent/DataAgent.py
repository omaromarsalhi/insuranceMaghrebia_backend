from fastApi.data_agent.functions import check_connection, connect_to_db, init_nl2sql_engine, get_data_from_db
from fastApi.orchestration.utils import FunctionToolWithContext
from fastApi.orchestration.workflow import AgentConfig


class DataAgent(AgentConfig):
    def __init__(self):
        name = "Data Agent"
        description = "turn the user input into sql query and execute it"

        data_agent_prompt = """
            You are a Data Agent responsible for retrieving and processing database information. 
            You must operate autonomously and collaborate with other agents through the orchestrator.
            
            ### Responsibilities:
            
            1. **Database Connection**:
               - Use `check_connection` to verify the database status and `connect_to_db` if necessary.
               
            2. **Data Processing**:
               - Use `init_nl2sql_engine` to process the query if required.

            3. **Task Delegation**:
               - Use `RequestTransfer` to escalate tasks beyond your scope.

               

        """

        tools = [
            FunctionToolWithContext.from_defaults(async_fn=check_connection),
            FunctionToolWithContext.from_defaults(async_fn=connect_to_db),
            FunctionToolWithContext.from_defaults(async_fn=init_nl2sql_engine),
            FunctionToolWithContext.from_defaults(async_fn=get_data_from_db),
        ]

        super().__init__(name=name,
                         description=description,
                         system_prompt=data_agent_prompt,
                         tools=tools)
