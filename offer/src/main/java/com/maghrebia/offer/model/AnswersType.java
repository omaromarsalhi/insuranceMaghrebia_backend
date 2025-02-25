package com.maghrebia.offer.model;


import lombok.Builder;

@Builder
public record AnswersType(
        int questionIndex,
        String answerText
) {
}
