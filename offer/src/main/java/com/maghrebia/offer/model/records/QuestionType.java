package com.maghrebia.offer.model.records;


import lombok.Builder;

@Builder
public record QuestionType(
        String questionText
) {
}
