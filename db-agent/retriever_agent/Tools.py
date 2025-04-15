from llama_index.core.workflow import Context
from sqlalchemy import text

async def execute(ctx: Context) -> str:
    """Execute database queries with proper async session management"""
    database = await ctx.get("database")
    if not database:
        return "ERROR: Database connection not established"

    query = await ctx.get("query_result")
    print(query)
    session = None
    try:
        async with database.async_session() as session:
            async with session.begin():
                result = await session.execute(text(query))

                if query.strip().upper().startswith("SELECT"):
                    return [dict(row) for row in result.mappings()]

                await session.commit()
                return "Operation completed successfully"

    except Exception as e:
        if session:
            await session.rollback()
        return f"ERROR: {str(e)}"
    finally:
        if session:
            await session.close()

