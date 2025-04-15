from fastApi.sql_agent.Tools import check_connection, connect_to_db, init_nl2sql_engine, \
    get_SQL_from_user_query, execute
from fastApi.orchestration.utils import FunctionToolWithContext
from fastApi.orchestration.workflow import AgentConfig


class SQLAgent(AgentConfig):
    def __init__(self):
        name = "End-to-End SQL Processor"
        description = "Full lifecycle processing from natural language to executed SQL results"

        data_agent_prompt = """
        **SQL Processing Protocol v2.0**

        ### Core Workflow:
        1. Connection Verification:
           - ALWAYS start with `check_connection`
           - If disconnected: Trigger `connect_to_db` and re-verify
        
        2. Query Conversion:
           - Execute `get_SQL_from_user_query` for SQL generation
           - On success: Proceed to execution
           - On engine error: Trigger `init_nl2sql_engine` and retry
        
        3. Query Execution:
           - Final step: Pass generated SQL to `execute`
           - Return direct execution results to user

        ### Execution Rules:
        - MANDATORY SEQUENCE: 
          check_connection → [connect_to_db] → [init_nl2sql_engine] → get_SQL_from_user_query → execute
        - Never bypass connection checks
        - Automatic LIMIT 5 enforcement unless specified
        - Sanitize all user inputs

        ### Error Handling:
        - Connection failure: "CONNECTION_ERROR: Code 01"
        - SQL generation failure: "GENERATION_ERROR: Code 02"
        - Execution failure: Forward `execute` tool's error

        ### Output Protocol:
        Successful flow:
        ┌───────────────────────┐    ┌───────────────────────┐    ┌───────────────────────┐
        │  Generated SQL Query  │ →  │  Execution Results    │ →  │  Final Formatted Data │
        └───────────────────────┘    └───────────────────────┘    └───────────────────────┘

        ### Examples:
        User: "Show 5 Texas customers"
        1. check_connection → True
        2. get_SQL_from_user_query → "SELECT [...]"
        3. execute → [{"id": 1, "name": "John"}, ...]
        
        User: "December sales totals"
        1. check_connection → False
        2. connect_to_db → Success
        3. get_SQL_from_user_query → "SELECT [...]" 
        4. execute → [{"month": 12, "total": 150000}]
        """

        tools = [
            FunctionToolWithContext.from_defaults(async_fn=check_connection),
            FunctionToolWithContext.from_defaults(async_fn=connect_to_db),
            FunctionToolWithContext.from_defaults(async_fn=init_nl2sql_engine),
            FunctionToolWithContext.from_defaults(async_fn=get_SQL_from_user_query),
            FunctionToolWithContext.from_defaults(async_fn=execute),
        ]

        super().__init__(
            name=name,
            description=description,
            system_prompt=data_agent_prompt,
            tools=tools
        )