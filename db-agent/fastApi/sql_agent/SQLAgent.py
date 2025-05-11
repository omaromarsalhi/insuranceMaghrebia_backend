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

        ### Final Output Requirement:
        After processing is completed, output a single sentence summarizing the overall result. For example:
        - On success: "SUCCESS: The SQL query was executed and returned the expected results."
        - On error: "ERROR: <Description of the error encountered>."
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
# from fastApi.sql_agent.Tools import check_connection, connect_to_db, init_nl2sql_engine, \
#     get_SQL_from_user_query, execute
# from fastApi.orchestration.utils import FunctionToolWithContext
# from fastApi.orchestration.workflow import AgentConfig
#
# class SQLAgent(AgentConfig):
#     def __init__(self):
#         name = "End-to-End SQL Processor"
#         description = "Full lifecycle processing from natural language to executed SQL results"
#
#         data_agent_prompt = """
#         **SQL Processing Protocol v2.1**
#
#         ### Core Workflow:
#         1. Connection Verification:
#            - ALWAYS start with `check_connection`
#            - If disconnected: Trigger `connect_to_db` and re-verify
#
#         2. Engine Initialization:
#            - REQUIRED: Execute `init_nl2sql_engine` after successful connection
#            - On failure: Abort with "ENGINE_INIT_ERROR: Code 03"
#
#         3. Query Conversion:
#            - Execute `get_SQL_from_user_query` for SQL generation
#            - On success: Proceed to execution
#            - On error: Return "GENERATION_ERROR: Code 02"
#
#         4. Query Execution:
#            - Final step: Pass generated SQL to `execute`
#            - Return raw execution results to user
#
#         ### Execution Rules:
#         - MANDATORY SEQUENCE:
#           check_connection → [connect_to_db] → init_nl2sql_engine → get_SQL_from_user_query → execute
#         - Never bypass engine initialization
#         - Automatic LIMIT 5 enforcement unless specified
#         - Sanitize all user inputs
#
#         ### Error Handling:
#         - Connection failure: "CONNECTION_ERROR: Code 01"
#         - Engine initialization failure: "ENGINE_INIT_ERROR: Code 03"
#         - SQL generation failure: "GENERATION_ERROR: Code 02"
#         - Execution failure: Forward `execute` tool's error directly
#
#         ### Output Protocol:
#         Successful flow:
#         ┌───────────────────────┐    ┌───────────────────────┐    ┌───────────────────────┐
#         │  Engine Initialized   │ →  │  Generated SQL Query  │ →  │  Execution Results    │
#         └───────────────────────┘    └───────────────────────┘    └───────────────────────┘
#
#         ### Final Output Requirement:
#         After processing, output a single sentence summary:
#         - Success: "SUCCESS: SQL executed with valid results."
#         - Error: "ERROR: <Specific error code and description>."
#         """
#
#         tools = [
#             FunctionToolWithContext.from_defaults(async_fn=check_connection),
#             FunctionToolWithContext.from_defaults(async_fn=connect_to_db),
#             FunctionToolWithContext.from_defaults(async_fn=init_nl2sql_engine),
#             FunctionToolWithContext.from_defaults(async_fn=get_SQL_from_user_query),
#             FunctionToolWithContext.from_defaults(async_fn=execute),
#         ]
#
#         super().__init__(
#             name=name,
#             description=description,
#             system_prompt=data_agent_prompt,
#             tools=tools
#         )