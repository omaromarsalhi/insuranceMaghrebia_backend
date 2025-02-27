package com.maghrebia.offer.dto.helpers;

import com.maghrebia.offer.validation.FormFieldDtoValid;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.List;

@Builder
@FormFieldDtoValid
public record FormFieldDto(

        @NotBlank(message = "Label is required")
        String label,

        @NotBlank(message = "Type is required")
        @Pattern(regexp = "^(text|email|date|time|select|checkbox|color|range|radio)$",
                message = "Invalid type. Allowed values: text, email, date, select, checkbox, radio")
        String type,

        @Min(value = 0, message = "Order must be at least 0")
        int order,

        boolean required,

        String placeholder,

        String regex,

        String regexErrorMessage,

        @Min(value = 0, message = "Range start must be at least 0")
        int rangeStart,

        @Min(value = 0, message = "Range end must be at least 0")
        int rangeEnd,

        List<String> selectOptions
) {
        @AssertTrue(message = "Range end must be greater than or equal to range start")
        public boolean isRangeValid() {
                // Example: Validate range only if type is 'number' (not present in current types)
                return rangeEnd >= rangeStart;
        }
}