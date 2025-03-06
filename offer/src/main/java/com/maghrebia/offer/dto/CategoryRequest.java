package com.maghrebia.offer.dto;

import com.maghrebia.offer.model.enums.CategoryTarget;
import lombok.Builder;

import java.util.Date;


@Builder
public record CategoryRequest(
        String name,
        String description,
        String imageUri,
        CategoryTarget categoryTarget
) {
}
