import asyncio
from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession
from sqlalchemy.orm import sessionmaker
from sqlalchemy import inspect, text
from fastApi.utils.Config import Config
from fastApi.utils.Database import Database

#
# class Database:
#     """Handles async database connections and setup."""
#
#     def __init__(self, config: Config):
#         try:
#             self.db_host = config.get('DATABASE', 'host')
#             self.db_user = config.get('DATABASE', 'user')
#             self.db_password = config.get('DATABASE', 'password')
#             self.db_port = config.get('DATABASE', 'port')
#             self.db_name = config.get('DATABASE', 'db_name')
#             self.engine = self.create_async_engine()
#             self.async_session = sessionmaker(
#                 bind=self.engine,
#                 class_=AsyncSession,
#                 expire_on_commit=False
#             )
#         except Exception as e:
#             raise RuntimeError(f"Database initialization failed: {str(e)}")
#
#     def create_async_engine(self):
#         """Create and return an async SQLAlchemy engine."""
#         return create_async_engine(
#             f'postgresql+asyncpg://{self.db_user}:{self.db_password}@{self.db_host}:{self.db_port}/{self.db_name}',
#             echo=True  # Enable for debugging
#         )
#
# async def execute(query: str) -> str:
#     """Execute database queries with proper async session management"""
#     session = None
#     try:
#         config = Config('config.ini')
#         database = Database(config)
#
#         async with database.async_session() as session:
#             async with session.begin():
#                 result = await session.execute(text(query))
#
#                 if query.strip().upper().startswith("SELECT"):
#                     return [dict(row) for row in result.mappings()]
#
#                 await session.commit()
#                 return "Operation completed successfully"
#
#     except Exception as e:
#         if session:
#             await session.rollback()
#         return f"ERROR: {str(e)}"
#     finally:
#         if session:
#             await session.close()
#
# async def main():
#     query = "SELECT * FROM appointment LIMIT 1;"
#     try:
#         result = await execute(query)
#         print(result)
#     except Exception as e:
#         print(f"Execution failed: {str(e)}")
#
# if __name__ == "__main__":
#     asyncio.run(main())


config = Config('config.ini')
database = Database(config)

query = "SELECT * FROM appointment LIMIT 1;"

session = None
try:
    async_session = sessionmaker(
        bind=database.get_engine(),
        class_=AsyncSession,
        expire_on_commit=False
    )
    with async_session as session:
        with session.begin():

            result = session.execute(text(query))
            print(result)
            if query.strip().upper().startswith("SELECT"):
                print([dict(row) for row in result.mappings()])

            session.commit()

except Exception as e:
    if session:
        session.rollback()
finally:
    if session:
        session.close()
