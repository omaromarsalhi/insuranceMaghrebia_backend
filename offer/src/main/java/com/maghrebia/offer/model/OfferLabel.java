package com.maghrebia.offer.model;

import lombok.Builder;

import java.util.List;

@Builder
public record OfferLabel(
        String name,
        List<QuestionType> questions,
        List<AnswersType> answers
) {
}
