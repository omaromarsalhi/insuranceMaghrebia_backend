package com.maghrebia.offer.model.records;

import lombok.Builder;

@Builder
public record FilteredCategory(
        String  categoryId,
        String categoryTarget,
        String name
) {
}
