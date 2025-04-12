from llama_index.core import PromptTemplate

prompt_template_str = """
    You are an insurance pricing expert. 
    Explain how the user's input affects their premium **using using the rules below and what you know if the is no rule**. Never invent new rules or percentages.
    
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
             title: string; // put the factor in a user friendly way with the word 'Factor' as a title
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
    1. Identify the EXACT rule matching the factor and value.
    2. State the impact (e.g., "+20%") **verbatim** from the rules.
    3. Explain the reason **word-for-word** from the rules.
    4. Use 1 sentence. No markdown. Example:
       "Your defensive driving course reduces your premium by 15% because certified drivers demonstrate safer habits."
    </Task>
    <Critical Instructions>
        - Respond ONLY with the JSON object. 
        - No markdown code blocks (no ```json or ```).
        - No additional text before/after the JSON.
        - Example BAD response: 
          ```json
          { "title": "...", "content": "..." }
    </Critical Instructions>
"""

prompt = PromptTemplate(prompt_template_str)

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
    "defensiveDrivingCourse": {
        "impact": "-15%",
        "reason": "Certified drivers demonstrate safer habits",
        "code_reference": "if (request.defensiveDrivingCourse()) risk -= 0.15f;"
    },
    "vehicleType": {
        "car": "500 TND",
        "motorcycle": "700 TND",
        "truck": "900 TND",
        "reason": """car reason: Cars have moderate risk due to average accident rates, standard personal use, and medium repair costs.
                    motorcycle reason: Motorcycles pose higher risk with less rider protection, higher injury rates, and more accident-prone behavior.
                    truck reason: "Trucks carry the highest risk due to their size, commercial use, potential for high damage, and cargo liability."""
    },
    "ageDepreciation": {
        "calculation": "-1.5% per year of vehicle age",
        "reason": "Older vehicles have lower repair costs",
        "code_reference": "base *= (1 - (vehicleAge * 0.015f))"
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
    "engineRisk": {
        "calculation": "+3% per liter of displacement (max +15%)",
        "reason": "Larger engines enable higher speeds",
        "code_reference": "Math.min(engineSize * 0.03f, 0.15f)"
    },
    "weightClass": {
        "5000lb": "+8%",
        "6000lb": "+12%",
        "reason": "Heavier vehicles cause more damage",
        "code_reference": "parseWeightClassRisk()"
    },
    "urbanArea": {
        "impact": "+25%",
        "reason": "High traffic density and theft rates"
    },
    "ruralArea": {
        "impact": "+10%",
        "reason": "Lower population density risks"
    },
    "codeReference": "isUrbanArea() check",
    "precipitation": {
        "threshold": "5mm average",
        "impact": "+15%",
        "reason": "Wet conditions increase accident risk",
        "code_reference": "if (averagePrecipitation > 5.0f)"
    },
    "temperatureVariation": {
        "calculation": "+2% per °C daily variation",
        "reason": "Extreme swings affect vehicle performance",
        "code_reference": "tempVariation * 0.02f"
    },
    "windRisk": {
        "base_impact": "+1% per km/h average wind",
        "high_wind_days": "+5% per day >30km/h winds",
        "reason": "Strong winds increase accident likelihood",
        "code_reference": "calculateWindRisk()"
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
    }
}
