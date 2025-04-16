from llama_index.llms.gemini import Gemini
from llama_index.embeddings.gemini import GeminiEmbedding
from llama_index.core.settings import Settings

class CheckDescription:
    def __init__(self, google_api_key, model="models/gemini-1.5-flash"):
        self.google_api_key = google_api_key
        self.llm = Gemini(
            model=model,
            api_key=self.google_api_key
        )
        Settings.embed_model = GeminiEmbedding(api_key=self.google_api_key)
        Settings.llm = self.llm

    def ask_gemini(self, title, description):
        """Check the compatibility between the title and description of a complaint"""
        prompt = f"""
    I will provide you with a title and a description of a complaint related to insurance services.

1. First, check if the title and description resemble a valid complaint.  
   - A valid complaint is a statement expressing dissatisfaction about a service, product, or experience.  
   -A valid complaint should concern insurance services, such as policy coverage, claims processing, customer support, or contract disputes.
   - If the input does not look like a real complaint (e.g., random words, unrelated topics, test inputs), respond with:  
     "Invalid complaint: Please provide a valid issue."

2. If the input seems like a valid complaint, check if the description matches the title:  
   - If the description is consistent with the title, respond: "Yes".  
   - If not, respond: "No" and explain in one sentence why the description doesn't match the title.  
   - Then, suggest an alternative title that better fits the description.
    
        1. Complaint Title:
        {title}
        2. Complaint Description:
        {description}
        """

        gemini_response = self.llm.complete(prompt=prompt)
        return gemini_response.text