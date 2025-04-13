from llama_index.core import PromptTemplate

prompt_template_str = """
    You are an insurance pricing expert.
    Explain how the user's input affects their premium **using the rules below and what you know if the is no rule or you think that the rule doesnt much the factor**. Never invent percentages.

    <Rules>
    {insurance_rules}
    </Rules>

    <User Input>
    Factor: {factor}
    Value: {value}
    </User Input>


    <Response Format Requirements>
        1. Return a JSON object matching this TypeScript interface:
           interface AiInsight {
             title: string; // put the factor in a user friendly way
             content: string; // Explanation using verbatim rules
             impact?: 'positive' | 'negative' | 'neutral'; // Derived from % change
           }
        2. Determine 'impact' by:
           - 'positive' if rule decreases premium (e.g., "-15%")
           - 'negative' if rule increases premium (e.g., "+25%")
           - 'neutral' if no change (e.g., "+0%")
        3. Content must:
           - State exact percentage from rules
           - Include word-for-word reason from rules
           - Be 1 sentence max
    </Response Format Requirements>
    <Task>
        1. **Rule Matching**: Identify the EXACT rule for the factor and value provided.
        2. **Impact Statement**: State the impact (e.g., "+20%") **verbatim** from the rules.
        3. **Reason Adaptation**: 
           - Use the rule's exact reason if clear and user-friendly.
           - Rephrase **only for clarity** while preserving the original meaning.
        4. **Personalization**: 
           - Include the user’s specific input value (e.g., "4 years").
           - Avoid generic phrases like "statistically" or "data shows".
        5. **Output Format**: 
           - 2 sentence max. 
           - No markdown. 
           - Example: "Your 4 years of driving experience reduce your premium by 20% because experienced drivers have fewer accidents."
    </Task>
"""
prompt = PromptTemplate(prompt_template_str)

vin_list = ['grossVehicleWeightRating', 'modelYear', 'displacementL', 'safetyFeatures']
weather_list = ['precipitation', 'temperatureVariation','windRisk']

INSURANCE_RULES = {
    "drivingExperience": {
        "calculation": "-5% per year of experience (max -20%)",
        "allowed_impacts": ["-5%", "-10%", "-15%", "-20%"],
        "reason": "More experienced drivers have lower accident rates",
        "code_reference": "risk -= Math.min(experience * 0.05f, 0.20f)"
    },
    "accidentHistory": {
        "0_accidents": {
            "impact": "+0%",
            "reason": "No recent accident history"
        },
        "1_accident": {
            "impact": "+20%",
            "reason": "Single accident in past 3 years"
        },
        "2+_accidents": {
            "impact": "+25%",
            "reason": "Multiple accidents indicate higher risk"
        },
        "code_reference": "if (request.accidentHistory().contains(\"+\")) risk += 0.25f;"
    },
    "trafficViolations": {
        "impact": "+10%",
        "reason": "Traffic violations correlate with claim likelihood",
        "code_reference": "if (request.trafficViolations()) risk += 0.1f;"
    },
    "vehicleType": {
        "car": "500 TND",
        "motorcycle": "700 TND",
        "truck": "900 TND",
        "reason": """car reason: Cars have moderate risk due to average accident rates, standard personal use, and medium repair costs.
                    motorcycle reason: Motorcycles pose higher risk with less rider protection, higher injury rates, and more accident-prone behavior.
                    truck reason: "Trucks carry the highest risk due to their size, commercial use, potential for high damage, and cargo liability."""
    },
    "coverageType": {
        "basic": {
            "multiplier": "1.0x",
            "impact": "Base rate"
        },
        "comprehensive": {
            "multiplier": "1.4x",
            "impact": "+40%",
            "reason": "Full protection coverage"
        },
        "third-party": {
            "multiplier": "0.8x",
            "impact": "-20%",
            "reason": "Limited liability coverage"
        },
        "code_reference": "getCoverageMultiplier()"
    },
    "defensiveDrivingCourse": {
        "impact": "-15%",
        "reason": "Certified drivers demonstrate safer habits",
        "code_reference": "if (request.defensiveDrivingCourse()) risk -= 0.15f;"
    },

    "grossVehicleWeightRating": {
        "5000lb": "+8%",
        "6000lb": "+12%",
        "reason": "Heavier vehicles cause more damage",
    },
    "modelYear": {
        "calculation": "-1.5% per year of vehicle age",
        "reason": "Older vehicles have lower repair costs",
        "code_reference": "base *= (1 - (vehicleAge * 0.015f))"
    },
    "displacementL": {
        "calculation": "+3% per liter of displacement (max +15%)",
        "reason": "Larger engines enable higher speeds",
        "code_reference": "Math.min(engineSize * 0.03f, 0.15f)"
    },
    "safetyFeatures": {
        "per_feature": "-4%",
        "features": [
            "ABS",
            "Traction Control",
            "Electronic Stability Control",
            "Backup Camera",
            "Daytime Running Lights"
        ],
        "reason": "Safety systems reduce accident severity",
        "code_reference": "risk -= safetyFeatures * 0.04f"
    },

    "geographic_factors": {
        "urbanArea": {
            "impact": "+25%",
            "calculation": "risk += 0.25f if isUrbanArea(address)",
            "condition": "isUrbanArea(address) == true",
            "reason": "Urban areas have higher premiums due to increased traffic density and theft risks",
            "code_reference": "calculateGeographicRisk()"
        },
        "ruralArea": {
            "impact": "+10%",
            "calculation": "risk += 0.10f if isUrbanArea(address) == false",
            "reason": "Rural areas have moderately lower risks than urban zones",
            "code_reference": "calculateGeographicRisk()"
        }
    },

    "precipitation": {
        "threshold": ">5.0mm average",
        "impact": "+15%",
        "calculation": "weatherRisk += 0.15f",
        "reason": "Areas with heavy rainfall have higher accident rates",
        "code_reference": "if (filteredWeather.averagePrecipitation() > 5.0f)"
    },
    "temperatureVariation": {
        "calculation": "+2% per °C daily variation",
        "formula": "tempVariation * 0.02f",
        "reason": "Large temperature swings affect tire pressure and engine performance",
        "code_reference": "tempVariation = (averageMaxTmp - averageMinTmp)"
    },
    "windRisk": {
        "base_calculation": "+1% per km/h average wind",
        "high_wind_penalty": "+5% per day with winds >30km/h",
        "formula": "windRisk = (averageWind * 0.01f) + (highWindDays * 0.05f)",
        "reason": "Strong winds increase vehicle control challenges",
        "code_reference": "calculateWindRisk()",
        "components": {
            "averageWind": {
                "calculation": "filtered.averageWind() * 0.01f"
            },
            "highWindDays": {
                "definition": "Days with windspeed >30km/h",
                "calculation": "count * 0.05f"
            }
        }
    }

}
