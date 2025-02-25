package com.maghrebia.offer.dto.helpers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record FilteredCategoryDto(
//        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
        String categoryId,

        @NotBlank
        @Pattern(regexp = "^(PARTICULAR|COMPANY)$", message = "Invalid category target")
        String categoryTarget,

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z\\s]{2,50}$", message = "Invalid category name")
        String name
) {
}
