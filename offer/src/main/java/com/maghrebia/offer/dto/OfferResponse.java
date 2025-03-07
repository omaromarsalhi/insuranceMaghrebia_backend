package com.maghrebia.offer.dto;

import com.maghrebia.offer.dto.helpers.BenefitTypeDto;
import com.maghrebia.offer.dto.helpers.FilteredCategoryDto;
import com.maghrebia.offer.dto.helpers.OfferLabelDto;
import com.maghrebia.offer.dto.helpers.OfferPackageDto;
import lombok.Builder;

import java.util.List;

@Builder
public record OfferResponse(
        String offerId,
        String name,
        String header,
        String imageUri,
        String formId,
        List<BenefitTypeDto> benefits,
        FilteredCategoryDto category,
        List<OfferLabelDto> labels,
        List<OfferPackageDto> packages
) {
}
