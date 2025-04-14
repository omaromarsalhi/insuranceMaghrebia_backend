from llama_index.core.workflow import Context
from fastApi.data_agent.Config import Config
from fastApi.data_agent.Database import Database
from fastApi.data_agent.Nl2SqlEngine import Nl2SqlEngine
from fastApi.orchestration.workflow import ProgressEvent




async def check_connection(ctx: Context):
    """Verifies if a valid connection to the database exists by checking the context for the 'database'.

    Returns:
        bool: True if a valid database connection is established, otherwise False.
    """
    database = await ctx.get("database", None)
    if database is None:
        return False
    return True


async def connect_to_db(ctx: Context) -> str:
    """Establishes a connection to the database and stores the connection in the context for future use.

    Returns:
        str: A message confirming the success of the database connection.
    """
    config = Config("../config.ini")
    ctx.write_event_to_stream(ProgressEvent(msg="connecting to the database..."))
    database = Database(config)
    await ctx.set("config", config)
    await ctx.set("database", database)
    return "Database connection established."


async def init_nl2sql_engine(ctx: Context) -> str:
    """Initializes the NL2SQL engine, enabling the conversion of natural language queries into SQL queries for database retrieval.

    Returns:
        str: A message confirming the initialization of the NL2SQL engine.
    """
    config = await ctx.get("config")
    database = await ctx.get("database")
    nl2sql_engine = Nl2SqlEngine(config, database)
    await ctx.set("nl2sql_engine", nl2sql_engine)
    return "NL2SQL engine initialized."


async def get_data_from_db(ctx: Context, user_query: str):
    """Retrieves data from the database based on the user's natural language query by converting it into an appropriate SQL query.

    Returns:
        str: Query results as formatted string
    """
    nl2sql_engine = await ctx.get("nl2sql_engine")
    query_result = nl2sql_engine.query(user_query)
    return query_result




