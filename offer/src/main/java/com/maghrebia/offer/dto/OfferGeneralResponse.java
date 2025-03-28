package com.maghrebia.offer.dto;


import lombok.Builder;

@Builder
public record OfferGeneralResponse(
        String offerId,
        String status
) {
}
