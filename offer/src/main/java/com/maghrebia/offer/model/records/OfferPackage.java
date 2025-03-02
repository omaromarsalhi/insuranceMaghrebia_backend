package com.maghrebia.offer.model.records;

import lombok.Builder;

import java.util.List;


@Builder
public record OfferPackage(
        String customDuration,
        String duration,
        List<String> features,
        float price,
        String title
) {
}
