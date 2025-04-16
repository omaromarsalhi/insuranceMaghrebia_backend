import os
import json
from fastapi import FastAPI, UploadFile, File, Form, HTTPException
from fastapi.responses import JSONResponse
from pydantic import BaseModel
from dotenv import load_dotenv
import google.generativeai as genai
from PyPDF2 import PdfReader
from io import BytesIO
import uvicorn

# Load environment variables
load_dotenv()
api_key = os.getenv("GEMINI_API_KEY")
genai.configure(api_key=api_key)

# Create FastAPI app
app = FastAPI()

# Pydantic schema for job metadata (keep existing structure)
class ModelRequest(BaseModel):
    title: str
    description: str
    minimumYearsOfExperience: int
    location: str
    jobType: str
    skillsRequired: list[str]
@app.post("/")
def start_root():
    print("FASTAPI STARTED")


@app.post("/analyze-resume")
async def analyze_resume(
    modelRequest: str = Form(...),
    file: UploadFile = File(...)
    ):
    try:
        # Parse job JSON from string
        job_dict = json.loads(modelRequest)
        job = ModelRequest(**job_dict)

        # Process PDF in memory
        pdf_content = await file.read()
        pdf_text = extract_text_from_pdf(BytesIO(pdf_content))

        # Generate analysis using Gemini directly
        response = analyze_with_gemini(pdf_text, job)
        
        return {"result": response}

    except json.JSONDecodeError:
        raise HTTPException(400, "Invalid JSON format for modelRequest")
    except Exception as e:
        return JSONResponse(status_code=500, content={"error": f"Server error: {str(e)}"})

def extract_text_from_pdf(pdf_file):
    """Extract text from PDF file in memory"""
    try:
        reader = PdfReader(pdf_file)
        text = "\n".join([page.extract_text() for page in reader.pages])
        return text
    except Exception as e:
        raise HTTPException(400, f"Failed to process PDF: {str(e)}")

def analyze_with_gemini(resume_text, job: ModelRequest):
    """Analyze resume using Gemini API directly"""
    model = genai.GenerativeModel('gemini-1.5-pro-latest')
    
    job_type_rules = {
        "FULL_TIME": "Prioritize professional experience and production-grade projects",
        "INTERNSHIP": "Academic projects and internships count as experience",
        "CONTRACT": "Focus on project deliverables and technical depth",
        "PART_TIME": "Flexible experience requirements, emphasize skill match"
    }

    prompt = f"""
    Analyze this resume for the following position:
    
    **Position**: {job.title}
    **Type**: {job.jobType}
    **Location**: {job.location}
    **Description**: {job.description}
    **Required Skills**: {', '.join(job.skillsRequired)}
    **Min Experience**: {job.minimumYearsOfExperience}
    
    Job Type Considerations:
    {job_type_rules.get(job.jobType, '')}
    
    Resume Content:
    {resume_text}
    
    Provide analysis in this exact JSON format:
    {{
    "fitScore": "X/10",
    "strengths": ["..."],
    "weaknesses": ["..."],
    "recommendation": "Strong Hire | Conditional Hire | Not a Fit"
    }}
    """

    try:
        response = model.generate_content(prompt)
        return response.text
    except Exception as e:
        raise RuntimeError(f"Gemini API error: {str(e)}")

if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=8040)