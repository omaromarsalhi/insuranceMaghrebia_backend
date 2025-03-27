package com.maghrebia.offer.model.records;


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
        String javaRegex,
        String regexErrorMessage,
        int rangeStart,
        int rangeEnd,
        List<String> selectOptions
) {
}
