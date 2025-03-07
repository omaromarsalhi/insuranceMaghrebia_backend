package com.maghrebia.offer.mapper;

import com.maghrebia.offer.dto.CategoryRequest;
import com.maghrebia.offer.dto.CategoryResponse;
import com.maghrebia.offer.model.OfferCategory;


public class CategoryMapper {

    public static CategoryResponse toCategoryResponse(OfferCategory category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .name(category.getName())
                .description(category.getDescription())
                .categoryTarget(category.getCategoryTarget())
                .imageUri(category.getImageUri())
                .createdAt(category.getCreatedAt())
                .build();
    }

    public static OfferCategory toOfferCategory(CategoryRequest request) {
        return OfferCategory.builder()
                .categoryTarget(request.categoryTarget())
                .name(request.name())
                .description(request.description())
                .imageUri(request.imageUri())
                .build();
    }
}
