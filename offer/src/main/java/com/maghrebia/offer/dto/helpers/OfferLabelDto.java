package com.maghrebia.offer.dto.helpers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record OfferLabelDto(
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9\\s\\-]{2,50}$", message = "Invalid label name")
        String name,

        @Size(min = 1)
        List<@Valid QuestionTypeDto> questions,

        @Size(min = 1)
        List<@Valid AnswersTypeDto> answers
) {
}
