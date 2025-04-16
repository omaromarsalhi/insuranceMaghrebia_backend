import google.generativeai as gemini
from insurance_explainer.auto_variables import prompt, INSURANCE_RULES
import asyncio
from dotenv import load_dotenv
import os

load_dotenv()


class ChatBotManager:
    def __init__(self):
        self.api_key = os.getenv("GOOGLE_API_KEY")
        gemini.configure(api_key=self.api_key)
        self.model = gemini.GenerativeModel(model_name='models/gemini-2.0-flash')

    async def chat(self, factor: str, value: str) -> str:
        """Handle chat with session-specific memory"""
        try:

            formatted_prompt = prompt.format(
                insurance_rules=INSURANCE_RULES[factor],
                factor=factor,
                value=value
            )

            # Get AI response
            response = await asyncio.to_thread(
                self.model.generate_content,
                formatted_prompt
            )
            assistant_response = response.text

            return assistant_response

        except Exception as e:
            return f"AI Error: {str(e)}"




# user_input = {
#     "factor": "driving_experience",
#     "value": "4",
# }
# chatbot = ChatBotManager()
# # response = await chatbot.chat(**user_input)
# # print(f"Explanation for driving experience: {response}")
#
# import asyncio
#
#
# # your async function
# async def main():
#     response = await chatbot.chat(**user_input)
#     print(response)
#
#
# # run the async function
# asyncio.run(main())
