from llama_index.core import SQLDatabase, VectorStoreIndex, Settings
from llama_index.core.indices.struct_store import SQLTableRetrieverQueryEngine
from llama_index.core.objects import SQLTableNodeMapping, SQLTableSchema, ObjectIndex
from llama_index.embeddings.google_genai import GoogleGenAIEmbedding
from llama_index.llms.google_genai import GoogleGenAI

from fastApi.sql_agent.prompt import custom_prompt
from fastApi.utils import Database
from fastApi.utils import Config


class Nl2SqlEngine:
    """Handles the LlamaIndex setup and query execution."""

    def __init__(self, config: Config, database: Database):

        self.gemini_key = config.get('API', 'gemini_key')
        Settings.llm = GoogleGenAI(api_key=self.gemini_key,temperature=0.0)
        Settings.embed_model = GoogleGenAIEmbedding(api_key=self.gemini_key)


        tables = database.get_tables()
        self.sql_database = SQLDatabase(database.engine)

        table_node_mapping = SQLTableNodeMapping(self.sql_database)
        table_schema_objs = [SQLTableSchema(table_name=table) for table in tables]


        self.obj_index = ObjectIndex.from_objects(
            table_schema_objs,
            table_node_mapping,
            VectorStoreIndex
        )

        self.query_engine = SQLTableRetrieverQueryEngine(
            text_to_sql_prompt=custom_prompt,
            sql_database=self.sql_database,
            synthesize_response=False,
            table_retriever=self.obj_index.as_retriever(similarity_top_k=3),
            sql_only=True
        )


    def query(self, prompt: str):
        """Query the LlamaIndex engine and return the response."""
        try:
            # return self.query_engine.query(prompt)
            return self.query_engine.query(prompt).response
        except ValueError as e:
            return "Something went wrong, please try again."