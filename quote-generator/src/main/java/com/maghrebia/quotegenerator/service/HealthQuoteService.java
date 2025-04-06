package com.maghrebia.quotegenerator.service;

import com.maghrebia.quotegenerator.dto.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.maghrebia.quotegenerator.service.RiskConstants.*;


@Service
@AllArgsConstructor
public class HealthQuoteService {


    private final WHOService whoService;

    public QuoteResponse calculate(HealthInsuranceRequest request) {

        float annualPremium = calculateAnnualPremium(request);

        return QuoteResponse.builder()
                .baseAnnualPremium(annualPremium)
                .amount(annualPremium)
                .currency("TND")
                .billingPeriod(BillingPeriod.ANNUAL)
                .validFrom(LocalDate.now())
                .validTo(LocalDate.now().plusYears(1))
                .build();
    }


    private float calculateAnnualPremium(HealthInsuranceRequest request) {
        float basePremium = getBasePremium(request.planType());
        float riskFactor = 1.0f;

        riskFactor += calculateMedicalRisk(request);
        riskFactor += calculateLifestyleRisk(request);
        riskFactor += calculateRegionalRisk(request.governorate());
        riskFactor += calculateOccupationRisk(request.occupation());

        List<WHOApiResponse> whoApiResponses;
        if (request.gender().equals(Gender.MALE.toString()))
            whoApiResponses = whoService.getWHOStats(Gender.MALE);
        else
            whoApiResponses = whoService.getWHOStats(Gender.FEMALE);

        riskFactor += calculateNationalRisk(whoApiResponses);

        float deductibleDiscount = DEDUCTIBLE_DISCOUNTS.getOrDefault(request.deductible(), 0.0f);

        float premium = basePremium * riskFactor * (1 - deductibleDiscount);
        premium += calculateAddOns(request.addOns());

        return premium;
    }


    private float calculateNationalRisk(List<WHOApiResponse> whoApiResponses) {
        float totalRisk = 0.0f;

        for (WHOApiResponse whoApiResponse : whoApiResponses) {
            float risk;
            WHOApiResponse.WHOData whoData = whoApiResponse.value().get(0);

            if (whoData.numericValue() != null) {
                float confidenceScore;
                if (whoData.high() != null && whoData.low() != null) {
                    float range = whoData.high() / 100 - whoData.low() / 100;
                    if (range < BENCHMARK_RANGES.get(whoData.indicatorCode()))
                        confidenceScore = 1 - range / BENCHMARK_RANGES.get(whoData.indicatorCode());
                    else
                        confidenceScore = Math.max(0.25f, BENCHMARK_RANGES.get(whoData.indicatorCode()) / range);
                } else {
                    confidenceScore = BENCHMARK_RANGES.get(whoData.indicatorCode());
                }

                float effectiveWeight = BASE_WEIGHTS.get(whoData.indicatorCode()) * confidenceScore;

                int dataAge = LocalDate.now().getYear() - whoData.timeDim();

                float temporalWeight;

                if (dataAge <= 2) temporalWeight = 1.0f;
                else if (dataAge <= 5) temporalWeight = 0.8f - (0.1f * (dataAge - 2));
                else temporalWeight = 0.5f - (0.04f * (dataAge - 5));
                temporalWeight = Math.max(0.3f, temporalWeight);


                risk = (whoData.numericValue() / 100) * effectiveWeight * temporalWeight;

            } else {
                risk = BASE_RURAL_PENALTY;
            }


            totalRisk += risk;
        }
        return totalRisk;
    }

    private float getBasePremium(String planType) {
        return PLAN_PRICES.getOrDefault(planType, 1200.0f);
    }

    private float calculateMedicalRisk(HealthInsuranceRequest request) {
        float risk = 0.0f;

        for (String condition : request.preExistingConditions()) {
            risk += switch (condition) {
                case "Hypertension" -> 0.20f;
                case "Diabetes" -> 0.25f;
                case "Heart Disease" -> 0.30f;
                case "Cancer" -> 0.40f;
                default -> 0.10f;
            };
        }

        for (String condition : request.familyHistory()) {
            risk += switch (condition) {
                case "Diabetes" -> 0.10f;
                case "Heart Disease" -> 0.15f;
                case "Cancer" -> 0.20f;
                default -> 0.05f;
            };
        }

        if (request.bmi() >= 30.0f) risk += 0.20f;
        else if (request.bmi() >= 25.0f) risk += 0.10f;

        if ("Yes".equalsIgnoreCase(request.chronicIllnesses())) risk += 0.15f;
        if ("Yes".equalsIgnoreCase(request.hospitalizations())) risk += 0.10f;
        if ("Yes".equalsIgnoreCase(request.surgeries())) risk += 0.10f;

        return risk;
    }

    private float calculateLifestyleRisk(HealthInsuranceRequest request) {
        float risk = 0.0f;

        if ("Yes".equalsIgnoreCase(request.smoking())) risk += 0.15f;

        switch (request.alcohol()) {
            case "Occasional" -> risk += 0.05f;
            case "Regular" -> risk += 0.10f;
        }

        switch (request.exercise()) {
            case "1–3x per week" -> risk -= 0.05f;
            case "4–7x per week" -> risk -= 0.10f;
        }

        return risk;
    }

    private float calculateRegionalRisk(String governorate) {
        return RURAL_GOVERNORATES.contains(governorate) ? 0.15f : 0.0f;
    }

    private float calculateOccupationRisk(String occupation) {
        return switch (occupation) {
            case "Laborer" -> 0.20f;
            case "Student" -> -0.05f;
            case "Unemployed" -> 0.10f;
            default -> 0.0f;
        };
    }

    private float calculateAddOns(List<String> addOns) {
        return addOns.stream()
                .map(addon -> ADDON_PRICES.getOrDefault(addon, 0.0f))
                .reduce(0.0f, Float::sum);
    }

}

