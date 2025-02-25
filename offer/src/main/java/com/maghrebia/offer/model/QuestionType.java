package com.maghrebia.offer.model;


import lombok.Builder;

@Builder
public record QuestionType(
        String questionText
) {
}
