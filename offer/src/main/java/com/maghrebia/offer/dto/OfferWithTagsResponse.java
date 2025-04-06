package com.maghrebia.offer.dto;

import com.maghrebia.offer.dto.helpers.BenefitTypeDto;
import com.maghrebia.offer.dto.helpers.FilteredCategoryDto;
import com.maghrebia.offer.dto.helpers.OfferLabelDto;
import com.maghrebia.offer.dto.helpers.OfferPackageDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OfferWithTagsResponse(
        String offerId,
        String name,
        String imageUri,
        String category,
        List<String> tags
) {
}
