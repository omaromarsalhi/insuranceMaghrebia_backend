from llama_index.core import PromptTemplate
from llama_index.core.prompts import PromptType

sql_gen_prompt = (
    "**You are a PostgreSQL SQL query generator.** Your ONLY task is to return "
    "a valid SELECT statement based on the schema and user question. "
    "**Never explain, describe, or return anything except SQL.**\n\n"

    "**Critical Rules:**\n"
    "- OUTPUT ONLY THE SQL QUERY\n"
    "- STRICTLY PROHIBITED: Natural language, explanations, markdown, or non-SQL content\n"
    "- ONLY USE THESE CLAUSES: SELECT, FROM, WHERE, ORDER BY, LIMIT, OFFSET\n"
    "- FORBIDDEN COMMANDS: INSERT/UPDATE/DELETE, DDL, transactions, or schema modifications\n"
    "- DEFAULT TO SELECT * UNLESS columns are explicitly specified\n"
    "- ENFORCE LIMIT 5 WHENEVER RESULTS ARE REQUESTED\n"
    "- USE CASE-INSENSITIVE FILTERS: LOWER() or ILIKE for text comparisons\n"
    "- USE CURRENT_DATE/INTERVAL FOR DATE FILTERS\n\n"

    "**Schema Context:**\n"
    "{schema}\n\n"

    "**Examples:**\n"
    "User: 3 customers from Texas\n"
    "SQL: SELECT * FROM customers WHERE LOWER(state) = 'texas' LIMIT 3;\n\n"

    "User: Orders after March with product names\n"
    "SQL: SELECT o.order_id, p.product_name FROM orders o JOIN products p ON o.product_id = p.id WHERE order_date > '2024-03-01' LIMIT 5;\n\n"

    "**Current Task:**\n"
    "User: {query_str}\n"
    "SQL:"
)

custom_prompt = PromptTemplate(
    sql_gen_prompt,
    prompt_type=PromptType.TEXT_TO_SQL,
)

# Response prompt remains unchanged but ensure execution layer validates SQL
response_prompt = (
    "Given an input question, synthesize a response from the PostgreSQL query results.\n"
    "Query: {query_str}\n"
    "SQL: {sql_query}\n"
    "SQL Response: {context_str}\n"
    "Response: "
    "If the question does not relate to the database, return 'This question is not related to the database'.\n\n"
    "If the SQL query involves restricted operations (DELETE, INSERT, UPDATE, DROP, TRUNCATE, ALTER, CREATE), "
    "respond with 'Sorry, this operation is not allowed'. Otherwise, provide the result of the query."
)

response_prompt = PromptTemplate(
    response_prompt,
    prompt_type=PromptType.SQL_RESPONSE_SYNTHESIS_V2,
)
