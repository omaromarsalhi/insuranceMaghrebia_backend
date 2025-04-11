# from fastapi import FastAPI, HTTPException, Request
# from fastapi.middleware.cors import CORSMiddleware
# from fastapi.responses import JSONResponse
# from pydantic import BaseModel
# import json
# # from formAgent import agent
# # from prompts import form_generate_prompt
# # from utils import clean_json_response
# #
# #
# # # Initialize FastAPI app
# # app = FastAPI()
# #
# # # Configure CORS
# # app.add_middleware(
# #     CORSMiddleware,
# #     allow_origins=["http://localhost:4300"],
# #     allow_credentials=True,
# #     allow_methods=["*"],
# #     allow_headers=["*"],
# # )
# #
# # # Request model for chat input
# # class ChatRequest(BaseModel):
# #     user_input: str
# #
# # # Chat endpoint
# # @app.post("/chat/")
# # async def chat_with_agent(request: ChatRequest):
# #     if request.user_input.lower() == "exit" or request.user_input.lower() == "close":
# #         return JSONResponse(content={"type": "text","responseText": "Goodbye!"})
# #     formatted_input = form_generate_prompt.format(user_input=request.user_input )
# #     try:
# #         response = str(agent.chat(formatted_input))
# #         response=clean_json_response(response)
# #         if response.startswith("[") or response.startswith("```json") :
# #             cleaned_response = response.strip("```json").strip("```").strip()
# #             parsed_json = json.loads(cleaned_response)
# #             return JSONResponse(content={"type": "json","responseText": parsed_json})
# #         return JSONResponse(content={"type": "text","responseText": response})
# #     except Exception as e:
# #         raise HTTPException(status_code=500, detail=str(e))
# #
# # if __name__ == "__main__":
# #     import uvicorn
# #     uvicorn.run(app, host="0.0.0.0", port=9100)








from fastapi import FastAPI, HTTPException, Request
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from pydantic import BaseModel
import json
import logging
from formAgent import agent
from prompts import form_generate_prompt
from utils import clean_json_response

# Initialize FastAPI app
app = FastAPI()



@app.get("/")
def read_root():
    return {"Hello": "World"}

# Configure logging
logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)

# Configure CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:4200","http://localhost:4300"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Request model for chat input
class ChatRequest(BaseModel):
    user_input: str

@app.middleware("http")
async def log_requests(request: Request, call_next):
    try:
        if request.method == "POST":
            body = await request.body()
            logger.debug(f"Request body: {body.decode()}")
    except Exception as e:
        logger.error(f"Error logging request: {e}")

    response = await call_next(request)
    return response

# Chat endpoint
@app.post("/chat/")
async def chat_with_agent(request: ChatRequest):
    logger.debug(f"Received chat request with input: {request.user_input}")

    if request.user_input.lower() in ["exit", "close", "quit"]:
        logger.debug("Received exit command")
        return JSONResponse(content={"type": "text", "responseText": "Goodbye!"})

    try:
        response = str(agent.chat(request.user_input))
        response=str(clean_json_response(response))
        if response.startswith("[") or response.startswith("```json"):
            logger.debug("Detected JSON response")
            try:
                cleaned_response = response.replace("```json", "").replace("```", "").strip()
                logger.debug(f"Cleaned response: {cleaned_response}")

                parsed_json = json.loads(cleaned_response)
                logger.debug(f"Successfully parsed JSON: {parsed_json}")

                return JSONResponse(content={"type": "json", "responseText": parsed_json})
            except json.JSONDecodeError as e:
                logger.error(f"JSON parsing error: {e}")
                logger.error(f"Problematic response: {response}")
                raise HTTPException(
                    status_code=500,
                    detail=f"Failed to parse JSON response: {str(e)}"
                )

        logger.debug("Returning text response")
        return JSONResponse(content={"type": "text", "responseText": response})

    except Exception as e:
        logger.error(f"Error processing request: {e}", exc_info=True)
        raise HTTPException(
            status_code=500,
            detail=str(e)
        )

if __name__ == "__main__":
    import uvicorn
    logger.info("Starting FastAPI server in debug mode...")
    uvicorn.run(
        app,
        host="0.0.0.0",
        port=9100,
        log_level="debug",
        reload=True
    )


