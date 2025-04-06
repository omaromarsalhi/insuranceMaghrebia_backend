package com.maghrebia.offer.mapper;

import com.maghrebia.offer.dto.OfferRequest;
import com.maghrebia.offer.dto.OfferResponse;
import com.maghrebia.offer.dto.OfferUpdateRequest;
import com.maghrebia.offer.dto.OfferWithTagsResponse;
import com.maghrebia.offer.dto.helpers.*;
import com.maghrebia.offer.model.*;
import com.maghrebia.offer.model.records.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class OfferMapper {

    public static Offer toEntity(OfferRequest request) {
        List<OfferLabel> labels = request.labels().stream()
                .map(OfferMapper::toOfferLabelEntity)
                .toList();

        var benefits = request.benefits().stream().map(OfferMapper::toBenefitEntity).toList();

        var packages = request.packages().stream().map(OfferMapper::toPackageEntity).toList();

        var filteredCategory = toFilteredCategoryEntity(request.category());

        return Offer.builder()
                .name(request.name())
                .header(request.header())
                .imageUri(request.imageUri())
                .category(filteredCategory)
                .labels(labels)
                .isActive(true)
                .benefits(benefits)
                .tags(request.tags())
                .packages(packages)
                .formId(request.formId())
                .build();

    }

    public static Offer toUpdateEntity(OfferUpdateRequest request) {
        List<OfferLabel> labels = request.labels().stream()
                .map(OfferMapper::toOfferLabelEntity)
                .toList();

        var benefits = request.benefits().stream().map(OfferMapper::toBenefitEntity).toList();

        var packages = request.packages().stream().map(OfferMapper::toPackageEntity).toList();

        var filteredCategory = toFilteredCategoryEntity(request.category());

        return Offer.builder()
                .offerId(request.offerId())
                .name(request.name())
                .header(request.header())
                .imageUri(request.imageUri())
                .category(filteredCategory)
                .labels(labels)
                .isActive(request.isActive())
                .benefits(benefits)
                .packages(packages)
                .tags(request.tags())
                .formId(request.formId())
                .createdAt(request.createdAt())
                .build();

    }

    public static FilteredCategory toFilteredCategoryEntity(FilteredCategoryDto dto) {
        return FilteredCategory.builder()
                .categoryId(dto.categoryId())
                .categoryTarget(dto.categoryTarget())
                .name(dto.name())
                .build();
    }

    private static OfferLabel toOfferLabelEntity(OfferLabelDto dto) {

        List<AnswersType> answers = new ArrayList<>();
        List<QuestionType> questions = new ArrayList<>();

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

    private static OfferPackage toPackageEntity(OfferPackageDto dto) {
        return OfferPackage.builder()
                .customDuration(dto.customDuration())
                .title(dto.title())
                .price(dto.price())
                .duration(dto.duration())
                .features(dto.features())
                .build();
    }

    private static BenefitType toBenefitEntity(BenefitTypeDto dto) {
        return BenefitType.builder()
                .benefitText(dto.benefitText())
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


    public static OfferResponse toDto(Offer entity) {
        List<OfferLabelDto> labels = entity.getLabels().stream()
                .map(OfferMapper::toOfferLabelDto)
                .collect(Collectors.toList());

        var benefits = entity.getBenefits().stream().map(OfferMapper::toBenefitDto).toList();

        var packages = entity.getPackages().stream().map(OfferMapper::toPackageDto).toList();

        var filteredCategory = toFilteredCategoryDto(entity.getCategory());

        return OfferResponse.builder()
                .offerId(entity.getOfferId())
                .name(entity.getName())
                .header(entity.getHeader())
                .imageUri(entity.getImageUri())
                .formId(entity.getFormId())
                .category(filteredCategory)
                .isActive(entity.isActive())
                .benefits(benefits)
                .labels(labels)
                .tags(entity.getTags())
                .packages(packages)
                .createdAt(entity.getCreatedAt())
                .build();
    }


    public static OfferWithTagsResponse toTagDto(Offer entity) {

        return OfferWithTagsResponse.builder()
                .offerId(entity.getOfferId())
                .name(entity.getName())
                .imageUri(entity.getImageUri())
                .category(entity.getCategory().name())
                .tags(entity.getTags())
                .build();
    }

    public static FilteredCategoryDto toFilteredCategoryDto(FilteredCategory entity) {
        return FilteredCategoryDto.builder()
                .categoryId(entity.categoryId())
                .categoryTarget(entity.categoryTarget())
                .name(entity.name())
                .build();
    }

    private static OfferLabelDto toOfferLabelDto(OfferLabel entity) {
        List<AnswersTypeDto> answers = entity.answers().stream()
                .map(OfferMapper::toAnswerDto)
                .collect(Collectors.toList());

        List<QuestionTypeDto> questions = entity.questions().stream()
                .map(OfferMapper::toQuestionDto)
                .collect(Collectors.toList());

        return OfferLabelDto.builder()
                .name(entity.name())
                .questions(questions)
                .answers(answers)
                .build();
    }

    private static QuestionTypeDto toQuestionDto(QuestionType entity) {
        return QuestionTypeDto.builder()
                .questionText(entity.questionText())
                .build();
    }

    private static BenefitTypeDto toBenefitDto(BenefitType entity) {
        return BenefitTypeDto.builder()
                .benefitText(entity.benefitText())
                .build();
    }

    private static OfferPackageDto toPackageDto(OfferPackage entity) {
        return OfferPackageDto.builder()
                .customDuration(entity.customDuration())
                .title(entity.title())
                .price(entity.price())
                .duration(entity.duration())
                .features(entity.features())
                .build();
    }

    private static AnswersTypeDto toAnswerDto(AnswersType entity) {
        return AnswersTypeDto.builder()
                .questionIndex(entity.questionIndex())
                .answerText(entity.answerText())
                .build();
    }
}
