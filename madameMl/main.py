from fastapi import FastAPI
from pydantic import BaseModel
import pandas as pd
from joblib import load

# Load the pre-trained KNN model and preprocessor
knn = load('knn_model.joblib')
preprocessor = load('preprocessor.joblib')

app = FastAPI()


class InsuranceData(BaseModel):
    age: int
    sex: int
    weight: int
    bmi: float
    hereditary_diseases: int
    no_of_dependents: int
    smoker: int
    bloodpressure: int
    diabetes: int
    regular_ex: int
    job_title: int
    city_lat: float
    city_lon: float


@app.post("/predict")
def predict(data: InsuranceData):
    # Convert input data to DataFrame
    input_data = pd.DataFrame([dict(data)])
    processed_data = input_data.values.reshape(1, -1)
    # Make prediction
    prediction = knn.predict(processed_data)
    return {"prediction": int(prediction[0])}

