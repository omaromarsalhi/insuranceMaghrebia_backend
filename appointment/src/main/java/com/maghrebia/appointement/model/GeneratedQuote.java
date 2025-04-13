package com.maghrebia.appointement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "generatedquote")
public class GeneratedQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer generatedQuoteId;

    float amount;
    String billingPeriod;
    LocalDate validFrom;
    LocalDate validTo;
    String currency;
    float baseAnnualPremium;
}
