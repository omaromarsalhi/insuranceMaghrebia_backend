package com.maghrebia.offer.dto;

import com.maghrebia.offer.dto.helpers.BenefitTypeDto;
import com.maghrebia.offer.dto.helpers.FilteredCategoryDto;
import com.maghrebia.offer.dto.helpers.OfferLabelDto;
import com.maghrebia.offer.dto.helpers.OfferPackageDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


import java.util.List;


public record OfferRequest(
        @NotBlank(message = "Offer name is required")
        @Pattern(
                regexp = "^[a-zA-Z0-9\\s\\-.,'()]{3,100}$",
                message = "Name must be 3-100 characters (letters, numbers, spaces, or basic punctuation)"
        )
        String name,

        @NotBlank(message = "Offer header is required")
        @Pattern(
                regexp = "^[a-zA-Z0-9\\s\\-.,'()]{5,200}$",
                message = "Header must be 5-200 characters (letters, numbers, spaces, or basic punctuation)"
        )
        String header,

        String imageUri,

        @NotNull @Valid FilteredCategoryDto category,

        @Valid List<@Valid OfferLabelDto> labels,

        List<String> tags,

        List<BenefitTypeDto> benefits,

        List<OfferPackageDto> packages,

        String formId
) {}
