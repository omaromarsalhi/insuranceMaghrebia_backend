package com.maghrebia.quotegenerator.dto;

import lombok.Builder;

import java.time.LocalDate;


@Builder
public record QuoteResponse(
        float amount,
        BillingPeriod billingPeriod,
        LocalDate validFrom,
        LocalDate validTo,
        String currency,
        float baseAnnualPremium
) {
}
