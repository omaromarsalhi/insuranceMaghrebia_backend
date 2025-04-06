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

    public static final List<String> RURAL_GOVERNORATES = List.of("Kasserine", "Gafsa", "Tataouine");

    public static final Map<String, Float> ADDON_PRICES = Map.of(
            "Dental", 150.0f,
            "Vision", 100.0f,
            "Epidemic Coverage", 200.0f,
            "Mental Health", 80.0f
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
}
