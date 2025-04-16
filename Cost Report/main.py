from typing import Union

from fastapi import FastAPI

import getpass
import os

os.environ['OPENAI_API_KEY'] = getpass.getpass("OpenAI API Key: ")

app = FastAPI()
#llx-EWzez76Gdz9kgDcakJeDHjRh8At0M69ByvUAsBDoxcMaEwCO

@app.get("/")
def read_root():
    return {"bye world": "World"}


@app.get("/items/{item_id}")
def read_item(item_id: int, q: Union[str, None] = None):
    return {"item_id": item_id, "q": q}