from llama_index.llms.gemini import Gemini
from llama_index.embeddings.gemini import GeminiEmbedding
from llama_index.core.settings import Settings
import random
# seed = random.randint(1000, 9999)

class TitleGenerator:
    def __init__(self, google_api_key, model="models/gemini-1.5-flash"):
        self.google_api_key = google_api_key
        self.llm = Gemini(
            model=model,
            api_key=self.google_api_key
        )
        Settings.embed_model = GeminiEmbedding(api_key=self.google_api_key)
        Settings.llm = self.llm
        self.history = {}  # Stocke les titres déjà générés pour chaque description

    def ask_gemini(self, description):
        """Generate a compatible title based on the given description"""
        
        # Créer un identifiant unique pour la description
        description_key = description.strip().lower()
        
        if description_key not in self.history:
            self.history[description_key] = set()  # Stocke les réponses déjà données

        variation_factor = random.randint(1000, 9999)

        prompt = f"""
        I will provide you with a description of a complaint.
        Your task is to generate a **unique, descriptive, and creative title** that summarizes the issue.
        
        **Rules:**
        - **Never** repeat the same title for the same description.
        - Each time this function is called, the title **must be different**, even if the description remains unchanged.
        - Be **varied** in wording, structure, and synonyms.
        - Do not generate a simple list of similar titles—return only **one new title** each time.
        
        **Previously suggested titles for this description:** {", ".join(self.history[description_key])}

        **Complaint Description:**
        {description}

        **Provide a completely new and unique title** for this complaint (ID: {variation_factor}):
        **Do not include any numbers or IDs in your response. Just return the title.**
        """

        gemini_response = self.llm.complete(prompt=prompt)
        new_title = gemini_response.text.strip()
        if new_title in self.history[description_key]:
            return self.ask_gemini(description)  
        self.history[description_key].add(new_title)
        
        return new_title


#     def ask_gemini(self, description): 
        
#      """Generate a compatible title based on the given description"""
#      variation_factor = random.randint(1000, 9999) 
#      prompt = f"""
#     I will provide you with a description of a complaint.
#     Your task is to generate a **unique, descriptive, and creative title** that summarizes the issue.
    
#     **Rules:**
#     - **Never** repeat the same title for the same description.
#     - Each time this function is called, the title **must be different**, even if the description remains unchanged.
#     - Be **varied** in wording, structure, and synonyms.
#     - Do not generate a simple list of similar titles—return only **one new title** each time.
#     **Examples:**
#     - Description: "My phone is broken, I can't use it."
#       → First call: "Phone Completely Unusable After Malfunction"
#       → Second call: "Device Failure: Phone Not Working at All"
#       → Third call: "Smartphone Issue: Cannot Power On or Use"
#     **Complaint Description:**
#     {description}

#     **Provide a completely new and unique title** for this complaint (ID: {variation_factor}):
#      """

#      gemini_response = self.llm.complete(prompt=prompt)
#      return gemini_response.text
