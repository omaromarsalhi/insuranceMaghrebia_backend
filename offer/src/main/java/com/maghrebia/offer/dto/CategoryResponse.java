package com.maghrebia.offer.dto;

import com.maghrebia.offer.model.enums.CategoryTarget;
import lombok.Builder;

import java.util.Date;

@Builder
public record CategoryResponse(
        String categoryId,
        String name,
        String description,
        String imageUri,
        CategoryTarget categoryTarget,
        Date createdAt
) {
}
