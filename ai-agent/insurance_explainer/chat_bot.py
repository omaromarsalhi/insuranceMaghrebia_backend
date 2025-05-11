import google.generativeai as gemini
from dotenv import load_dotenv

from insurance_explainer.auto_variables import prompt, AUTO_INSURANCE_RULES
import asyncio
import os

from insurance_explainer.helth_variables import HEALTH_INSURANCE_RULES

load_dotenv()


class ChatBotManager:
    def __init__(self):
        self.api_key = os.getenv("gemini_key")
        gemini.configure(api_key=self.api_key)
        self.model = gemini.GenerativeModel(model_name='models/gemini-2.0-flash')

    async def chat(self, factor: str, value: str, typeQ: str) -> str:
        """Handle chat with session-specific memory"""
        try:
            print('omar')
            insurance_rules = AUTO_INSURANCE_RULES if typeQ == 'auto' else HEALTH_INSURANCE_RULES
            print(factor)
            print(insurance_rules[factor])
            print(value)
            print(typeQ)
            formatted_prompt = prompt.format(
                insurance_rules=insurance_rules[factor],
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

