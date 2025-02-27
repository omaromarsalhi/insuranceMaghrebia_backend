package com.maghrebia.offer.model;


import lombok.Builder;

import java.util.List;

@Builder
public record FormField(
        String label,
        String type,
        int order,
        boolean required,
        String placeholder,
        String description,
        String regex,
        String regexErrorMessage,
        int rangeStart,
        int rangeEnd,
        List<String> selectOptions
) {
}
