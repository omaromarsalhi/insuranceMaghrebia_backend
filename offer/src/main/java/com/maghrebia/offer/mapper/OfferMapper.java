package com.maghrebia.offer.mapper;

import com.maghrebia.offer.dto.OfferRequest;
import com.maghrebia.offer.dto.helpers.AnswersTypeDto;
import com.maghrebia.offer.dto.helpers.FilteredCategoryDto;
import com.maghrebia.offer.dto.helpers.OfferLabelDto;
import com.maghrebia.offer.dto.helpers.QuestionTypeDto;
import com.maghrebia.offer.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class OfferMapper {

    public static Offer toEntity(OfferRequest request) {
        List<OfferLabel> labels = request.labels().stream()
                .map(OfferMapper::toOfferLabelEntity)
                .toList();

        var filteredCategory = toFilteredCategoryEntity(request.category());

        return Offer.builder()
                .name(request.name())
                .header(request.header())
                .imageUri(request.imageUri())
                .category(filteredCategory)
                .labels(labels)
                .form(OfferForm.builder().formId(request.formId()).build())
                .build();

    }

    public  static FilteredCategory toFilteredCategoryEntity(FilteredCategoryDto dto) {
        return FilteredCategory.builder()
                .categoryId(dto.categoryId())
                .categoryTarget(dto.categoryTarget())
                .name(dto.name())
                .build();
    }

    private static OfferLabel toOfferLabelEntity(OfferLabelDto dto) {

        List<AnswersType> answers=new ArrayList<>();
        List<QuestionType> questions=new ArrayList<>();

        for (int i = 0; i < dto.answers().size(); i++) {
            answers.add(toAnswerEntity(dto.answers().get(i)));
            questions.add(toQuestionEntity(dto.questions().get(i)));
        }

        return OfferLabel.builder()
                .name(dto.name())
                .questions(questions)
                .answers(answers)
                .build();
    }

    private static QuestionType toQuestionEntity(QuestionTypeDto dto) {
        return QuestionType.builder()
                .questionText(dto.questionText())
                .build();
    }

    private static AnswersType toAnswerEntity(AnswersTypeDto dto) {
        return AnswersType.builder()
                .questionIndex(dto.questionIndex())
                .answerText(dto.answerText())
                .build();
    }
}
