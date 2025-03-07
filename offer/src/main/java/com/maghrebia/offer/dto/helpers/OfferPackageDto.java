package com.maghrebia.offer.dto.helpers;

import lombok.Builder;

import java.util.List;


@Builder
public record OfferPackageDto(
        String customDuration,
        String duration,
        List<String> features,
        float price,
        String title
) {
}
