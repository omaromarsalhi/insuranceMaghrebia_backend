from llama_index.core import SQLDatabase, VectorStoreIndex, Settings
from llama_index.core.indices.struct_store import SQLTableRetrieverQueryEngine
from llama_index.core.objects import SQLTableNodeMapping, SQLTableSchema, ObjectIndex
from llama_index.embeddings.google_genai import GoogleGenAIEmbedding
from llama_index.llms.google_genai import GoogleGenAI

from fastApi.data_agent.Config import Config
from fastApi.data_agent.Database import Database
from fastApi.data_agent.prompt import custom_prompt
from sql_db_search.Nl2SqlPrompts import response_prompt

class Nl2SqlEngine:
    """Handles the LlamaIndex setup and query execution."""

    def __init__(self, config: Config, database: Database):

        self.gemini_key = config.get('API', 'gemini_key')
        Settings.llm = GoogleGenAI(api_key=self.gemini_key)
        Settings.embed_model = GoogleGenAIEmbedding(api_key=self.gemini_key)


        tables = database.get_tables()
        sql_database = SQLDatabase(database.engine)

        table_node_mapping = SQLTableNodeMapping(sql_database)
        table_schema_objs = [SQLTableSchema(table_name=table) for table in tables]


        self.obj_index = ObjectIndex.from_objects(
            table_schema_objs,
            table_node_mapping,
            VectorStoreIndex
        )

        self.query_engine = SQLTableRetrieverQueryEngine(
            sql_database=sql_database,
            table_retriever=self.obj_index.as_retriever(similarity_top_k=3),
            text_to_sql_prompt=custom_prompt,
            response_synthesis_prompt=response_prompt
        )


    def query(self, prompt: str):
        """Query the LlamaIndex engine and return the response."""
        try:
            # return self.query_engine.query(prompt)
            return self.query_engine.query(prompt)
        except ValueError as e:
            return "Something went wrong, please try again."