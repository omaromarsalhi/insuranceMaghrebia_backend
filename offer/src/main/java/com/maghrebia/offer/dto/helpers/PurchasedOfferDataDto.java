package com.maghrebia.offer.dto.helpers;


import lombok.Builder;

@Builder
public record PurchasedOfferDataDto(
        String fieldLabel,
        String fieldType,
        Object fieldValue
) {
}
