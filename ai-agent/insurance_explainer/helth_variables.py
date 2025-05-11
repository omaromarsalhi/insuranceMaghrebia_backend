HEALTH_INSURANCE_RULES = {
    "age": {
        "40-49": {
            "impact": "+10%",
            "calculation": "basePremium *= 1.1",
            "reason": "Increased health risks in middle age",
            "code_reference": "if (request.age() < 50 && request.age() > 40)"
        },
        "50+": {
            "impact": "+20%",
            "calculation": "basePremium *= 1.2",
            "reason": "Higher health risks for seniors",
            "code_reference": "else if (request.age() > 50)"
        }
    },

    "preExistingConditions": {
        "Hypertension": "+20%",
        "Diabetes": "+25%",
        "Heart Disease": "+30%",
        "Cancer": "+40%",
        "Other": "+10%",
        "reason": "Chronic conditions increase claim likelihood",
    },

    "familyHistory": {
        "Diabetes": "+10%",
        "Heart Disease": "+15%",
        "Cancer": "+20%",
        "Other": "+5%",
        "reason": "Genetic predisposition to health issues",
    },

    "chronicIllnesses": {
        "impact": "+15%",
        "condition": "Yes/No response",
        "reason": "Ongoing conditions require continuous treatment",
        "code_reference": "\"Yes\".equalsIgnoreCase(request.chronicIllnesses())"
    },

    "hospitalizations": {
        "impact": "+10%",
        "condition": "Yes/No response",
        "reason": "Past hospitalizations indicate elevated health risks",
        "code_reference": "\"Yes\".equalsIgnoreCase(request.hospitalizations())"
    },

    "surgeries": {
        "impact": "+10%",
        "condition": "Yes/No response",
        "reason": "Surgical history suggests significant past health issues",
        "code_reference": "\"Yes\".equalsIgnoreCase(request.surgeries())"
    },

    "bmi": {
        "25-29.9": "+10%",
        "30+": "+20%",
        "calculation": "BMI ≥25 threshold",
        "reason": "Higher BMI correlates with health complications",
    },

    "smoking": {
        "impact": "+15%",
        "reason": "Smoking increases respiratory/cardiac risks",
    },

    "alcohol": {
        "Occasional": "+5%",
        "Regular": "+10%",
        "reason": "Alcohol consumption impacts long-term health",
    },

    "exercise": {
        "Sedentary": {
            "impact": "+5%",
            "definition": "Little to no regular physical activity",
            "reason": "Sedentary lifestyle increases cardiovascular and metabolic risks",
        },
        "1–3x/week": {
            "impact": "-5%",
            "reason": "Moderate exercise reduces health risks"
        },
        "4–7x/week": {
            "impact": "-10%",
            "reason": "Frequent exercise significantly improves health outcomes"
        },
    },

    "governorate": {
        "impact": "+15%",
        "governorates": "RURAL_GOVERNORATES list is Kasserine, Gafsa ,Tataouine ,Kebili, Siliana ,Le Kef ,Jendouba ,Sidi Bouzid, Beja ,Zaghouan ,Tozeur",
        "reason": "Limited access to specialized healthcare",
    },

    "occupation": {
        "Laborer": "+20%",
        "Student": "-5%",
        "Unemployed": "+10%",
        "Default": "+0%",
        "reason": "Occupation physical demands and stress levels",
        "code_reference": "switch (request.occupation())"
    },

    "deductible": {
        "impact": " 500 -> 0%,1000 -> 5%, 2000 -> 10% ",
        "sliding_scale": "Higher deductibles = bigger discounts",
        "reason": "Cost-sharing reduces insurer liability",
    },

    "addOns": {
        "Dental": "+150 TND",
        "Vision": "+100 TND",
        "Maternity/Paternity": "+180 TND",
        "Mental Health": "+80 TND",
        "Critical Illness": "+220 TND",
        "International": "+250 TND",
        "calculation": "Sum of selected add-ons",
        "reason": "Additional coverage increases premium",
        "code_reference": "calculateAddOns()"
    },

    "travelFrequency": {
        "Rarely": {
            "impact": "+0%",
            "definition": "No regular international travel",
            "reason": "Minimal exposure to travel-related health risks"
        },
        "1–2x/year": {
            "impact": "+15%",
            "reason": "Moderate travel increases exposure to regional diseases and accidents",
            "code_reference": "case \"1–2x/year\" -> risk += 0.15f"
        },
        "Monthly": {
            "impact": "+10%",
            "reason": "Frequent travel requires lower adjustment than occasional travel due to likely better travel health precautions",
            "code_reference": "case \"Monthly\" -> risk += 0.1f"
        },
        "riskLogic": {
            "note": "Occasional travelers (1-2x/year) receive higher adjustment than frequent travelers",
            "explanation": "Frequent travelers are assumed to have better health precautions (vaccinations, travel insurance) than occasional travelers"
        }
    },

    "vaccinations": {
        "per_vaccine": "-2% base",
        "regional_match": "-1.5% bonus",
        "max_discount": "10%",
        "reason": "Vaccinations reduce disease-specific risks",
    },

    "planType": {
        "Basic": {
            "basePrice": "1200 TND",
            "coverage": "Essential medical services only",
            "code_reference": "PLAN_PRICES.getOrDefault(planType, 1200.0f)"
        },
        "Comprehensive": {
            "basePrice": "1800 TND",
            "coverage": "Includes specialist visits and diagnostics",
            "reason": "Mid-tier balance of coverage and cost",
            "code_reference": "PLAN_PRICES map lookup"
        },
        "Premium": {
            "basePrice": "2500 TND",
            "coverage": "Full coverage including private hospital rooms",
            "reason": "Highest level of coverage with luxury benefits",
            "code_reference": "PLAN_PRICES map lookup"
        },
    },

    "whoDataIntegration": {
        "calculation": "Indicator-based risk scoring",
        "components": {
            "confidenceScoring": {
                "rangeComparison": {
                    "ifRangeWithinBenchmark": "confidence = 1 - (range/benchmark)",
                    "ifRangeExceedsBenchmark": "confidence = max(0.25, benchmark/range)",
                    "missingRangeFallback": "Use benchmark value directly",
                    "example": {
                        "indicator": "M_Est_cig_curr",
                        "benchmark": 0.30,
                        "scenarios": [
                            "Range 0.25 → confidence = 1 - (0.25/0.30) = 0.17",
                            "Range 0.40 → confidence = 0.30/0.40 = 0.75"
                        ]
                    },
                    "code_reference": "confidenceScore calculations"
                }
            },

            "temporalWeighting": {
                "dataAgeFormula": {
                    "recentData": {
                        "condition": "≤2 years old",
                        "weight": "1.0",
                        "reason": "Maximum reliability for current data"
                    },
                    "midTermData": {
                        "condition": "3-5 years old",
                        "calculation": "0.8 - (0.1 × (age - 2))",
                        "example": "4 years old → 0.8 - (0.1×2) = 0.6"
                    },
                    "historicData": {
                        "condition": ">5 years old",
                        "calculation": "0.5 - (0.04 × (age - 5))",
                        "floor": "0.3 minimum weight",
                        "example": "8 years old → 0.5 - (0.04×3) = 0.38"
                    },
                    "code_reference": "temporalWeight calculations"
                }
            },

            "effectiveWeight": {
                "formula": "BASE_WEIGHTS[indicator] × confidenceScore",
                "example": {
                    "indicator": "HWF_0001",
                    "baseWeight": 0.001,
                    "confidence": 0.75,
                    "result": "0.00075"
                },
                "code_reference": "effectiveWeight calculation"
            },

            "riskPerIndicator": {
                "formula": "(numericValue / 100) × effectiveWeight × temporalWeight",
                "example": {
                    "value": 85.0,
                    "effectiveWeight": 0.00075,
                    "temporalWeight": 0.6,
                    "calculation": "(85/100) × 0.00075 × 0.6 = 0.0003825"
                },
                "code_reference": "risk calculation line"
            }
        },

        "fallbackMechanism": {
            "condition": "Missing numeric data",
            "penalty": "15% base risk",
            "code_reference": "risk = BASE_RURAL_PENALTY"
        },

        "aggregation": {
            "method": "Sum of all indicator risks",
            "exampleTotal": "0.15 (rural) + 0.00038 + 0.0012 = 0.15158",
            "code_reference": "totalRisk += risk"
        },

        "benchmarkReferences": {
            "source": "BENCHMARK_RANGES in RiskConstants",
            "benchmarkValues": [
                {
                    "indicator": "BP_03",
                    "value": 0.15,
                    "rationale": "Hypertension prevalence ±15% national variation"
                },
                {
                    "indicator": "NCD_BMI_30C",
                    "value": 0.12,
                    "rationale": "Obesity rates ±12% (stable metric)"
                },
                {
                    "indicator": "M_Est_tob_curr",
                    "value": 0.25,
                    "rationale": "Tobacco use ±25% (cultural/regional variance)"
                },
                {
                    "indicator": "M_Est_cig_curr",
                    "value": 0.30,
                    "rationale": "Cigarette consumption ±30% (high volatility)"
                },
                {
                    "indicator": "HWF_0001",
                    "value": 8.0,
                    "rationale": "Doctor availability ±8/10k population"
                },
                {
                    "indicator": "HWF_0006",
                    "value": 15.0,
                    "rationale": "Nurse availability ±15/10k population"
                },
                {
                    "indicator": "SA_0000001403",
                    "value": 0.20,
                    "rationale": "Mental health ±20% (conservative estimate)"
                },
                {
                    "indicator": "MH_25",
                    "value": 0.40,
                    "rationale": "General mental health indicator"
                }
            ],
        },

        "rationale": "Combines current WHO data with historical reliability assessments and regional healthcare metrics to calculate systemic risk factors affecting all policyholders in the region"
    }
}
