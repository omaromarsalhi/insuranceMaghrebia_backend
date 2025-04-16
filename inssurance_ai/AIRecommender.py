from pymongo import MongoClient
from llama_index.llms.gemini import Gemini
from llama_index.embeddings.gemini import GeminiEmbedding
from llama_index.core.settings import Settings
from datetime import datetime
import json

import os
from dotenv import load_dotenv
load_dotenv()
GOOGLE_API_KEY = os.getenv("GOOGLE_API_KEY")
MONGO_URI = os.getenv("MONGO_URI", "mongodb://localhost:27017")
client = MongoClient(MONGO_URI)
db = client["userAction"]
collection = db["userAction"]
class AIRecommender:
    def __init__(self, google_api_key, model="models/gemini-1.5-flash"):
        self.google_api_key = google_api_key
        self.llm = Gemini(model=model, api_key=self.google_api_key)
        Settings.embed_model = GeminiEmbedding(api_key=self.google_api_key)
        Settings.llm = self.llm

    def generate_recommendations(self, user_data):
        prompt = f"""
**User Behavioral Analysis**

**User Data:**  
{user_data}

**Context:**  
We are analyzing the actions taken by this user on our insurance website to determine their level of satisfaction and engagement. The goal is to identify their interests, detect any immediate needs or hesitations, and propose concrete actions to improve their experience.

**Analysis Objectives:**  
1. Thoroughly analyze all actions performed by the user to understand their interests and behaviors.  
2. Detect any signs of hesitation or dissatisfaction (e.g., visits to complaint-related pages) as well as indicators of interest.  
3. Evaluate the overall satisfaction of the user, categorizing them as "interested", "hesitant", "dissatisfied", or any combination (e.g., "interested, hesitant", "dissatisfied, hesitant", "interested, dissatisfied", or "interested, dissatisfied, hesitant") if applicable.
4.Provide numerical values representing the degree of classification for each category.

**Criteria:**  
- Actions on pages like "/reclamation" or "/camplaint" suggest dissatisfaction.  
- Spending significant time on certain pages or abandoning forms may indicate hesitation.  
- Engaging with premium content, filling out detailed forms, or exploring specific service information may indicate interest.

  **Reformulated Actions List:**  
Please reformulate and describe the following actions performed by the user in natural language. For example:
- Input: {{ "page": "home", "eventType": "SCROLLER", "action": "like_post", "timeSpent": 15, "createdAt": "2025-03-08T23:12:48.198" }}
- Output: "The user visited the home page, scrolled through the content, and liked a post for 15 seconds, on 2025-03-08 at 23:12:48."

**Recommended Actions:**  
- For a **dissatisfied** user: Provide two specific strategies that include direct contact measures. For example:  
  1. "Contact the user immediately via phone to discuss their issues, offer a dedicated resolution plan including potential refunds or expedited support."  
  2. "Schedule a personalized consultation to address their concerns and propose a tailored support plan."
  
- For a **hesitant** user: Provide two specific strategies focusing on follow-up communication. For example:  
  1. "Send a personalized follow-up email with a subject line like 'Exclusive Offer: Enjoy 10% Discount on Your Auto Insurance!' that details the benefits of the viewed offer and includes a clear call-to-action."  
  2. "Offer an invitation to schedule a call or chat session to clarify any doubts about the offer, highlighting special incentives."
  
- For an **interested** user: Provide two specific strategies to enhance engagement. For example:  
  1. "Send a detailed email outlining the benefits of the insurance product, including testimonials and personalized offers."  
  2. "Provide an invitation to a webinar or one-on-one consultation to explore additional premium benefits and answer any questions."

- For users with combined classifications, ensure that you provide two strategies for each classification type present.

**Output Format Requirement:**  
Please provide the answer in JSON format with the following keys:
- "userAnalysis": a brief summary of the analysis.
- "classification": a string that may include one or more labels (e.g., "interested", "hesitant", "dissatisfied", or a combination thereof) as applicable.
- "percentages": an object with the following keys:  
  - "interested": the percentage of interest (0-100%)  
  - "hesitant": the percentage of hesitation (0-100%)  
  - "dissatisfied": the percentage of dissatisfaction (0-100%)
- "actions": an object where each key is one of the classification types present ("dissatisfied", "hesitant", "interested") and its value is an array of exactly two objects, each containing:
  - "actionType": the type of action (e.g., "email", "call", "meeting").
  - "description": the recommended strategy for that classification type.
- "activityList": a list of user actions, each containing:  
  - "description": describing the actions in natural language, including the time spent on the action 
  - "time": the timestamp of the action in ISO format (e.g., "2025-03-08T23:12:48.198")

Return the JSON response exactly without additional text.
        """
        response = self.llm.complete(prompt)
        return self._parse_response(response.text)

    def _parse_response(self, raw_response):
        # Nettoyage robuste de la réponse
        json_str = raw_response.strip()
        
        # Suppression des marqueurs de code et autres artefacts
        for marker in ['```json', '```']:
            if json_str.startswith(marker):
                json_str = json_str[len(marker):]
            if json_str.endswith(marker):
                json_str = json_str[:-len(marker)]
        
        json_str = json_str.strip()

        try:
            decoder = json.JSONDecoder(strict=True)
            result, _ = decoder.raw_decode(json_str)
            return self._validate_structure(result)
        except (json.JSONDecodeError, ValueError) as e:
            raise ValueError(f"Erreur de parsing JSON: {str(e)}") from e

    def _validate_structure(self, data):
        # Validation de la structure requise
        required_keys = {"userAnalysis", "classification", "actions"}
        if not all(key in data for key in required_keys):
            raise ValueError("Structure JSON invalide - clés manquantes")
            
        # Validation des types
        if not isinstance(data['actions'], dict):
            raise ValueError("La clé 'actions' doit être un objet")
            
        return data
    


ai_recommender = AIRecommender(GOOGLE_API_KEY)
user_data = collection.find_one({"userId": "67a9157f0a6a1371dce93411"})
date_filter_start = datetime(2024, 3, 2, 0, 0, 0)  
user_data = collection.find_one({"userId": "67a9157f0a6a1371dce93411"})

if user_data:
    gemini_answer = ai_recommender.generate_recommendations(user_data)
    print(gemini_answer)
 
else:
    print("Utilisateur non trouvé dans la base de données")