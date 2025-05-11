package com.maghrebia.quotegenerator.service;

import java.util.List;
import java.util.Map;

public class RiskConstants {

    public static final float BASE_RURAL_PENALTY = 0.15f;

    public static final Map<String, Float> PLAN_PRICES = Map.of(
            "Basic", 800.0f,
            "Comprehensive", 1200.0f,
            "Premium", 1600.0f
    );

    public static final List<String> RURAL_GOVERNORATES = List.of(
            "Kasserine",
            "Gafsa",
            "Tataouine",
            "Kebili",
            "Siliana",
            "Le Kef",
            "Jendouba",
            "Sidi Bouzid",
            "Beja",
            "Zaghouan",
            "Tozeur"
    );

    public static final Map<String, Float> ADDON_PRICES = Map.of(
            "Dental", 150.0f,
            "Vision", 100.0f,
            "Maternity/Paternity", 180.0f,
            "Mental Health", 80.0f,
            "Critical Illness", 220.0f,
            "International", 250.0f
    );


    public static final Map<Integer, Float> DEDUCTIBLE_DISCOUNTS = Map.of(
            500, 0.0f,
            1000, 0.05f,
            2000, 0.10f
    );

    public static final Map<String, Float> BASE_WEIGHTS = Map.of(
            "BP_03", 0.002f,
            "NCD_BMI_30C", 0.0015f,
            "M_Est_tob_curr", 0.003f,
            "M_Est_cig_curr", 0.004f,
            "SA_0000001403", 0.005f,
            "HWF_0001", 0.001f,
            "HWF_0006", 0.0008f,
            "MH_25", 0.005f
    );

    public static final Map<String, Float> BENCHMARK_RANGES = Map.of(
            // Chronic Diseases (expressed as decimal percentages)
            "BP_03", 0.15f,       // Hypertension: Expect ±15% variation nationally
            "NCD_BMI_30C", 0.12f, // Obesity: Tighter ±12% range (more stable metric)
            // Lifestyle Factors
            "M_Est_tob_curr", 0.25f, // Tobacco: Wider ±25% (cultural/regional differences)
            "M_Est_cig_curr", 0.30f, // Cigarettes: ±30% (higher volatility)
            // Healthcare Access (absolute values per 10k population)
            "HWF_0001", 8.0f,    // Doctors: ±8/10k benchmark range
            "HWF_0006", 15.0f,   // Nurses: ±15/10k benchmark range
            // Mental Health
            "SA_0000001403", 0.20f, // Mental health: Conservative ±20%
            "MH_25", 0.40f
    );

    public static final Map<String, Float> VACCINATION_DISCOUNTS = Map.of(
            "Flu", 0.02f,          // 2% discount
            "COVID-19", 0.03f,     // 3% discount
            "Hepatitis B", 0.04f,  // 4% discount (high regional impact)
            "MMR", 0.015f,
            "Tuberculosis", 0.025f
    );

    public static final Map<String, List<String>> REGIONAL_DISEASE_RISKS = Map.ofEntries(
            Map.entry("Tunis", List.of("Flu", "COVID-19", "Respiratory Infections")),
            Map.entry("Ariana", List.of("Flu", "COVID-19")),
            Map.entry("Ben Arous", List.of("Flu", "COVID-19")),
            Map.entry("Manouba", List.of("Flu", "COVID-19")),
            Map.entry("Sousse", List.of("Flu", "COVID-19", "Tuberculosis")),
            Map.entry("Sfax", List.of("Flu", "Respiratory Infections")),
            Map.entry("Gabès", List.of("Respiratory Infections")),
            Map.entry("Monastir", List.of("Flu", "COVID-19")),
            Map.entry("Kasserine", List.of("Hepatitis B", "Leishmaniasis")),
            Map.entry("Gafsa", List.of("Hepatitis B", "Tuberculosis")),
            Map.entry("Tataouine", List.of("Hepatitis B", "Leishmaniasis")),
            Map.entry("Médenine", List.of("Hepatitis B", "Tuberculosis")),
            Map.entry("Kairouan", List.of("Leishmaniasis", "MMR")),
            Map.entry("Nabeul", List.of("Flu", "COVID-19")),
            Map.entry("Bizerte", List.of("Flu", "Respiratory Infections")),
            Map.entry("Jendouba", List.of("Leishmaniasis", "Tuberculosis")),
            Map.entry("Béja", List.of("Hepatitis B", "Leishmaniasis")),
            Map.entry("Siliana", List.of("Hepatitis B", "Leishmaniasis")),
            Map.entry("Tozeur", List.of("Hepatitis B")),
            Map.entry("Kebili", List.of("Hepatitis B"))
    );

}
