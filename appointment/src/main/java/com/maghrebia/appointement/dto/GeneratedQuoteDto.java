package com.maghrebia.appointement.dto;

import lombok.Builder;

import java.time.LocalDate;


@Builder
public record GeneratedQuoteDto(
        float amount,
        String billingPeriod,
        LocalDate validFrom,
        LocalDate validTo,
        String currency,
        float baseAnnualPremium
) {
}
