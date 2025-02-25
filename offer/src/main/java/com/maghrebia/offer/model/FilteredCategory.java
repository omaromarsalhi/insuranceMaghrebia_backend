package com.maghrebia.offer.model;

import lombok.Builder;

@Builder
public record FilteredCategory(
        String  categoryId,
        String categoryTarget,
        String name
) {
}
