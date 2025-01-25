package com.moquawel.tenderInvitation.response;

import com.moquawel.tenderInvitation.offer.Offer;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record PayloadResponse(
        int total,
        List<Offer> data
) {
}
