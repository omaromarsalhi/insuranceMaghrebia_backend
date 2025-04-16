import os
from tkinter import Image
from dotenv import load_dotenv
import google.generativeai as genai
from PIL import Image 
from typing import Union
from pydantic import BaseModel
import base64
from io import BytesIO
from fastapi.middleware.cors import CORSMiddleware

from fastapi import FastAPI, HTTPException
from fastapi.responses import JSONResponse
import json
app = FastAPI()
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:4200"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)
load_dotenv()
secret_key = os.environ.get("GEMINI_API_KEY")
genai.configure(api_key=secret_key)
encadrement= """
i need you to only accept photos or links to photos of damaged objects,
you need to look very carefully and give me an estimated repair cost,
the repair cost should be estimated as of the following:
repair estimate to each visibly damaged part (the parts that are certainly damaged),
repair estimate to each possibly damaged part (hidden parts),
total repair estimate range.
if the object (ie car, house ...) is not damaged respond by saying that you can't estimate the damage and explain why.
the estimaion should be tunisian dinar.
each object has attribute part, attribute possibly (certain or uncertain damage) and attribute estimated cost which is an object that has minCost and maxCost
i need an object that has two attributes:
one called certain which has aray of certain damaged
one called uncertain which has array of uncertain damaged.
the result should be in simple json form.
the language used should be french
"""
model = genai.GenerativeModel('gemini-2.0-flash')  # or 'gemini-pro-vision' for image-specific tasks

#Load the image
image_path = "./images/image.png"  # Adjust path if needed
image = Image.open(image_path)




class ImageRequest(BaseModel):
    image: str

@app.post("/process-image/")
async def process_image(request: ImageRequest):
    try:
        # Decode Base64 and convert to PIL Image
        image_data = base64.b64decode(request.image)
        pil_image = Image.open(BytesIO(image_data))
        
        # Process with Gemini
        response = model.generate_content(
            [
                encadrement.strip(),
                pil_image
            ]
          )        
        res = response.text.replace("```json", "").replace("```", "")
        return JSONResponse(
            status_code=200,
            content= json.loads(res)

            
            
        )
    except Exception as e:
        raise HTTPException(
            status_code=400,
            detail=f"Error processing image: {str(e)}"
        )
#Generate a detailed description
