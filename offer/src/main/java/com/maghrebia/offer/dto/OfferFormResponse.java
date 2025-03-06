package com.maghrebia.offer.dto;

import com.maghrebia.offer.dto.helpers.FormFieldDto;
import lombok.Builder;

import java.util.List;

@Builder
public record OfferFormResponse(
        String formId,
        List<FormFieldDto> fields
) {
}
