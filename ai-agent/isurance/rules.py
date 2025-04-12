

autoRules={
    "rules": {
        "driver_factors": {
            "driving_experience": {
                "calculation": "-5% per year of experience (max -20%)",
                "reason": "More experienced drivers have lower accident rates"
            },
            "accident_history": {
                "0_accidents": "+0%",
                "1_accident": "+20%",
                "2+_accidents": "+25%",
                "reason": "Higher accident frequency indicates greater risk"
            },
            "traffic_violations": {
                "any_violation": "+10%",
                "reason": "Violations correlate with higher claim likelihood"
            },
            "defensive_course": {
                "discount": "-15%",
                "reason": "Certified drivers demonstrate safer driving habits"
            }
        },

        "vehicle_factors": {
            "type_base_premiums": {
                "car": "500 TND base",
                "motorcycle": "700 TND base",
                "truck": "900 TND base",
                "reason": "Different vehicle types have varying risk profiles"
            },
            "age_depreciation": {
                "calculation": "-1.5% per year of vehicle age",
                "reason": "Older vehicles have lower repair costs"
            },
            "safety_features": {
                "per_feature": "-4%",
                "features_considered": ["ABS", "Traction Control", "Stability Control", "Backup Camera", "Daytime Lights"],
                "reason": "Safety systems reduce accident severity"
            },
            "engine_risk": {
                "calculation": "+3% per liter of displacement (max +15%)",
                "reason": "Larger engines enable higher speeds"
            },
            "weight_class": {
                "5000lb": "+8%",
                "6000lb": "+12%",
                "reason": "Heavier vehicles cause more damage in collisions"
            }
        },

        "geographic_factors": {
            "urban_area": "+25%",
            "rural_area": "+10%",
            "reason": "Urban areas have higher traffic density and theft rates"
        },

        "weather_factors": {
            "precipitation": {
                "threshold": "5mm average",
                "impact": "+15%",
                "reason": "Wet conditions increase accident risk"
            },
            "temperature_variation": {
                "calculation": "+2% per °C daily variation",
                "reason": "Extreme temperature swings affect vehicle performance"
            },
            "wind_risk": {
                "base": "+1% per km/h average wind",
                "high_wind_days": "+5% per day >30km/h winds",
                "reason": "Strong winds increase accident likelihood"
            }
        },

        "coverage_types": {
            "basic": "100% multiplier",
            "comprehensive": "+40%",
            "third-party": "-20%",
            "reason": "Different coverage levels have varying protection scope"
        }
    },

    "form_fields_mapping": {
        "vin_decoding": ["vehicle_type", "model_year", "safety_features"],
        "license_number": ["driver_identity_verification"],
        "location": ["geographic_risk_calculation"],
        "billing_period": ["payment_frequency_conversion"]
    }
}