from llama_index.core import PromptTemplate
from llama_index.core.prompts import PromptType

# sql_gen_prompt = (
#     "**Generate ONLY PostgreSQL SELECT queries.**\n"
#
#     "**RULES:**\n"
#     "- OUTPUT ONLY SQL - NO TEXT/MARKDOWN\n"
#     "- MANDATORY JOINS if IDs exist to fetch related data\n"
#     "- ALLOWED: SELECT, FROM, WHERE, ORDER BY, LIMIT, OFFSET\n"
#     "- BANNED: DML/DDL, explanations, transactions\n"
#     "- USE LOWER()/ILIKE for text, CURRENT_DATE for dates\n"
#
#     "**SCHEMA:**\n{schema}\n\n"
#
#     "**EXAMPLES:**\n"
#     "User: Customers in Texas\n"
#     "SQL: SELECT * FROM customers WHERE LOWER(state) = 'texas' ;\n\n"
#
#     "User: Orders with product names\n"
#     "SQL: SELECT o.*, p.product_name FROM orders o JOIN products p ON o.product_id = p.id;\n\n"
#
#     "**TASK:**\nUser: {query_str}\nSQL:"
# )
sql_gen_prompt = (
    "**Generate PostgreSQL SELECT queries with full-data joins.**\n\n"

    "**MANDATORY RULES:**\n"
    "- ALWAYS JOIN ALL TABLES: appointment → automobile  → location\n"
    "- ALWAYS JOIN ALL TABLES: appointment → generatedquote\n"
    "- SELECT ALL COLUMNS: a.*, auto.*, loc.*,gen.*\n"
    "- USE EXPLICIT ALIASES: a=appointment, auto=automobile, loc=location,gen=generatedquote\n"
    "- OUTPUT ONLY SQL - NO COMMENTS\n\n"

    "**SCHEMA:**\n{schema}\n\n"

    "**TASK:**\nUser: {query_str}\nSQL:"
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
