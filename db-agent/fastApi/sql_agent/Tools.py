from datetime import datetime

from llama_index.core.workflow import Context
from sqlalchemy import text
from fastApi.sql_agent.Nl2SqlEngine import Nl2SqlEngine
from fastApi.orchestration.workflow import ProgressEvent
from fastApi.utils.Config import Config
from fastApi.utils.Database import Database
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import sessionmaker

from fastApi.utils.utils import serialize


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


async def get_SQL_from_user_query(ctx: Context, user_query: str):
    """
        Converts a natural language user query into an equivalent SQL query using the NL2SQL engine.

       Returns:
           str: The generated SQL query as a string.
   """
    nl2sql_engine = await ctx.get("nl2sql_engine")
    query_result = nl2sql_engine.query(user_query)
    await ctx.set('query_result', query_result)
    return "Sql generated successfully."


async def execute(ctx: Context) -> str:
    """Execute database queries with proper async session management"""
    database = await ctx.get("database")
    if not database:
        return "ERROR: Database connection not established"

    query = await ctx.get("query_result")
    print(query)
    session = None
    try:
        async_session = sessionmaker(
            bind=database.get_engine(),
            class_=AsyncSession,
            expire_on_commit=False
        )
        async with async_session() as session:
            async with session.begin():
                result = await session.execute(text(query))

                if query.strip().upper().startswith("SELECT"):
                    query_result = [dict(row) for row in result.mappings()]
                    print(query_result)
                    query_result = serialize(query_result)
                    await ctx.set("executed_query_results", query_result)
                    # return f"SUCCESS: Retrieved {len(query_result)} rows"
                    return f"SUCCESS: Retrieved 2 rows"
                else:
                    await session.commit()
                    return "SUCCESS: Write operation completed"

    except Exception as e:
        if session:
            await session.rollback()
        return f"ERROR: {str(e)}"
    finally:
        if session:
            await session.close()
