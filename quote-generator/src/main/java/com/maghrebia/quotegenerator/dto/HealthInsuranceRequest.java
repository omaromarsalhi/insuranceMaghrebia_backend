package com.maghrebia.quotegenerator.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record HealthInsuranceRequest(
        int age,
        String gender,
        String governorate,
        String occupation,
        List<String> preExistingConditions,
        List<String> familyHistory,
        String medications,
        String hospitalizations,
        String chronicIllnesses,
        String surgeries,
        String smoking,
        String alcohol,
        String exercise,
        Float  bmi,
        String planType,
        int deductible,
        List<String> addOns,
        String existingInsurance,
        String employerInsurance,
        String travelFrequency,
        List<String> vaccinations,
        boolean gdprConsent
) {
}
