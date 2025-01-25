package com.moquawel.tenderInvitation.response;

import lombok.Builder;

@Builder
public record OfferResponse(
        int code,
        PayloadResponse payload
) {
}
