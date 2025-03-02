package com.maghrebia.offer.model.records;


import lombok.Builder;

@Builder
public record AnswersType(
        int questionIndex,
        String answerText
) {
}
