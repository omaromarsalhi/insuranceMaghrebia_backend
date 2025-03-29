package com.maghrebia.offer.dto;

import com.maghrebia.offer.dto.helpers.BenefitTypeDto;
import com.maghrebia.offer.dto.helpers.FilteredCategoryDto;
import com.maghrebia.offer.dto.helpers.OfferLabelDto;
import com.maghrebia.offer.dto.helpers.OfferPackageDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.List;


public record OfferUpdateRequest(
        String offerId,
        String name,
        String header,
        String imageUri,
        String formId,
        boolean isActive,
        List<BenefitTypeDto> benefits,
        FilteredCategoryDto category,
        List<OfferLabelDto> labels,
        List<String> tags,
        List<OfferPackageDto> packages,
        LocalDateTime createdAt
) {}
