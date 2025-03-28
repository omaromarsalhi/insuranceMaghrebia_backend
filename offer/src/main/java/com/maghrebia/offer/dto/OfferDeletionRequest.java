package com.maghrebia.offer.dto;


import lombok.Builder;

@Builder
public record OfferDeletionRequest(
        String offerId,
        String formId,
        boolean isOffer,
        boolean isForm,
        boolean isPurchasedOffers
) {
}
