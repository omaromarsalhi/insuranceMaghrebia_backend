from dotenv import load_dotenv
from flask import Flask, request, jsonify
from CheckDescription import CheckDescription
from TitleGenerator import TitleGenerator
from AIRecommender import AIRecommender
from pymongo import MongoClient
import os
app = Flask(__name__)
load_dotenv()
GOOGLE_API_KEY = os.getenv("GOOGLE_API_KEY")
gemini_service = CheckDescription(google_api_key=GOOGLE_API_KEY)
gemini_service2 = TitleGenerator(google_api_key=GOOGLE_API_KEY)
gemini_service3 = AIRecommender(google_api_key=GOOGLE_API_KEY)
MONGO_URI = os.getenv("MONGO_URI", "mongodb://localhost:27017")
client = MongoClient(MONGO_URI)
db = client["userAction"]
collection = db["userAction"]


@app.route('/check-compatibility', methods=['POST'])
def check_compatibility():
    """Endpoint pour vérifier la compatibilité entre l'offre et le CV"""
    data = request.get_json()
    title = data.get('title')
    description = data.get('description')
    if not title or not description:
        return jsonify({"error": "L'offre et le chemin du CV sont requis"}), 400

    response = gemini_service.ask_gemini(title, description)

    return jsonify({"response": response})

@app.route('/getTitle', methods=['POST'])
def getTitle():
    data = request.get_json()
    if data is None or 'description' not in data:
        print("Received data:", request.data)

        return jsonify({"error": "Description not provided"}), 400

    description = data['description']
    response = gemini_service2.ask_gemini(description)

    return jsonify({"response": response})

# @app.route('/recommendations/<user_id>', methods=['GET'])
# def get_recommendations(user_id):
#     user_data = collection.find_one({"userId": user_id})
    
#     if user_data:
#         recommendations = gemini_service3.generate_recommendations(user_data)
#         return jsonify(recommendations)
#     else:
#         return jsonify({"error": "User not found"}), 404

@app.route('/recommendations', methods=['POST'])
def get_recommendations():
    actions = request.get_json() 

    if actions:
        recommendations = gemini_service3.generate_recommendations(actions)
        return jsonify(recommendations)
    else:
        return jsonify({"error": "No actions provided"}), 400

if __name__ == '__main__':
    app.run()
