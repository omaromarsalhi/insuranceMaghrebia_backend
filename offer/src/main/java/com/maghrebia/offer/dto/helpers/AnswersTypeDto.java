package com.maghrebia.offer.dto.helpers;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record AnswersTypeDto(
        @Min(0)
        int questionIndex,

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9\\s\\-.,!?]{1,500}$", message = "Invalid answer format")
        String answerText
) {
}
