import os
from sqlalchemy import create_engine, inspect
from sqlalchemy.ext.asyncio import AsyncSession, create_async_engine
from fastApi.utils.Config import Config


class Database:
    """Handles database connections and setup."""
    def __init__(self):
        self.db_host = os.getenv('host')
        self.db_user = os.getenv('DATABASE', 'user')
        self.db_password = os.getenv('DATABASE', 'password')
        self.db_port = os.getenv('DATABASE', 'port')
        self.db_name = os.getenv('DATABASE', 'db_name')
        self.engine = self.create_engine()

    def create_engine(self):
        """Create and return a SQLAlchemy engine."""
        return create_engine(
            f'postgresql+psycopg2://{self.db_user}:{self.db_password}@{self.db_host}:{self.db_port}/{self.db_name}'
        )

    def get_tables(self):
        """Get all table names from the database."""
        inspector = inspect(self.engine)
        return inspector.get_table_names()

    def get_engine(self):
        return create_async_engine(
            f'postgresql+asyncpg://{self.db_user}:{self.db_password}@'
            f'{self.db_host}:{self.db_port}/{self.db_name}'
        )