package com.maghrebia.offer.service;

import com.maghrebia.offer.dto.OfferRequest;
import com.maghrebia.offer.dto.OfferResponse;
import com.maghrebia.offer.mapper.OfferMapper;
import com.maghrebia.offer.model.Offer;
import com.maghrebia.offer.model.enums.CategoryTarget;
import com.maghrebia.offer.model.records.FilteredCategory;
import com.maghrebia.offer.repository.OfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final OfferCategoryService offerCategoryService;


    public OfferResponse create(OfferRequest offer) {
        var savedOffer = offerRepository.save(OfferMapper.toEntity(offer));
        return OfferResponse.builder()
                .offerId(savedOffer.getOfferId())
                .name(savedOffer.getName())
                .header(savedOffer.getHeader())
                .category(offer.category())
                .imageUri(offer.imageUri())
                .labels(offer.labels())
                .formId(savedOffer.getFormId())
                .build();
    }


    public OfferResponse getOne(String categoryId) {
        var category = offerCategoryService.getOfferCategoryById(categoryId);
        var filteredCategory = FilteredCategory.builder()
                .categoryId(category.categoryId())
                .categoryTarget(category.categoryTarget().name())
                .name(category.name())
                .build();
        var offer = offerRepository.findOneByCategory(filteredCategory);
        return OfferMapper.toDto(offer);
    }
}