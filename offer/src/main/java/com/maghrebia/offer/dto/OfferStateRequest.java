package com.maghrebia.offer.dto;

public record OfferStateRequest(
        String offerId,
        boolean state
) {
}
