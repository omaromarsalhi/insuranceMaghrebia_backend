package com.maghrebia.offer.dto;

import com.maghrebia.offer.dto.helpers.FormFieldDto;
import jakarta.validation.Valid;

import java.util.List;

public record OfferFormUpdateRequest(
        String formId,
        List<FormFieldDto> fields
) {
}
