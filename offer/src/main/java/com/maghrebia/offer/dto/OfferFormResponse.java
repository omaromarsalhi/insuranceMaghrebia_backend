package com.maghrebia.offer.dto;

import com.maghrebia.offer.dto.helpers.FilteredCategoryDto;
import com.maghrebia.offer.dto.helpers.OfferLabelDto;
import lombok.Builder;

import java.util.List;

@Builder
public record OfferFormResponse(
        String formId
) {
}
