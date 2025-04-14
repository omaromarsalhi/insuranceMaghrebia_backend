from llama_index.core import PromptTemplate
from llama_index.core.prompts import PromptType
combined_prompt = (
    "You are an agent designed to interact with a PostgreSQL database. "
    "Given an input question, create a syntactically correct PostgreSQL query to run. "
    "Only execute the query and return the result, no explanations or descriptions. "
    "You must return only the result of the query, no other information.\n\n"

    "Default to SELECT * to return all columns unless the user specifically requests particular columns. "
    "If the user specifies columns or attributes to retrieve, only query for those specific fields.\n\n"

    "Always limit your query to at most 5 results unless the user explicitly specifies a different number. "
    "You can order the results by a relevant column to return the most interesting examples in the database.\n\n"

    "When comparing text values, always perform case-insensitive comparisons using LOWER() function or ILIKE operator. "
    "Never use case-sensitive comparisons (= or LIKE) for text values.\n\n"

    "Use PostgreSQL-specific functions where appropriate (e.g., CURRENT_DATE, INTERVAL syntax). "
    "Use standard SQL syntax compatible with PostgreSQL 15+.\n\n"

    "You must only use the information returned by the query to construct your final answer. "
    "You MUST double-check your query before executing it. If you get an error, rewrite the query and try again.\n\n"

    "Here are some examples of user inputs and their corresponding SQL queries:\n\n"
    "{examples}\n\n"

    "{schema}\n\n"
    "Question: {query_str}\n"
    "SQLQuery: "
)

examples = """
    User input: Show me all customer records
    SQL query: SELECT * FROM customers LIMIT 5;
    
    User input: Display 10 product entries with names and prices
    SQL query: SELECT product_name, price FROM products LIMIT 10;
    
    User input: Find orders from last week
    SQL query: SELECT * FROM orders WHERE order_date >= CURRENT_DATE - INTERVAL '7 days' LIMIT 5;
    
    User input: List customer emails from New York
    SQL query: SELECT email FROM customers WHERE LOWER(city) = 'new york' LIMIT 5;
    
    User input: Show me everything about our suppliers
    SQL query: SELECT * FROM suppliers LIMIT 5;
"""


custom_prompt = PromptTemplate(
    combined_prompt,
    prompt_type=PromptType.TEXT_TO_SQL,
)

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



