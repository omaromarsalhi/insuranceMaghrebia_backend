from sqlalchemy import create_engine, inspect
from sqlalchemy.ext.asyncio import AsyncSession, create_async_engine
from sqlalchemy.orm import sessionmaker
from fastApi.utils.Config import Config


class Database:
    """Handles database connections and setup."""
    def __init__(self, config: Config):
        self.db_host = config.get('DATABASE', 'host')
        self.db_user = config.get('DATABASE', 'user')
        self.db_password = config.get('DATABASE', 'password')
        self.db_port = config.get('DATABASE', 'port')
        self.db_name = config.get('DATABASE', 'db_name')
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