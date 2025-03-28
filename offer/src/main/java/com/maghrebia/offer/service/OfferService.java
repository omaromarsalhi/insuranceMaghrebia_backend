package com.maghrebia.offer.service;

import com.maghrebia.offer.dto.*;
import com.maghrebia.offer.mapper.OfferMapper;
import com.maghrebia.offer.model.Offer;
import com.maghrebia.offer.model.OfferForm;
import com.maghrebia.offer.model.PurchasedOffer;
import com.maghrebia.offer.model.records.FilteredCategory;
import com.maghrebia.offer.repository.OfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final OfferCategoryService offerCategoryService;
    private final MongoTemplate mongoTemplate;


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

    public List<OfferResponse> getAll() {
        var offer = offerRepository.findAll();
        return offer.stream()
                .map(OfferMapper::toDto)
                .toList();
    }

    public OfferGeneralResponse delete(OfferDeletionRequest request) {
        try {
            if (request.isForm())
                mongoTemplate.remove(Query.query(Criteria.where("_id").is(request.offerId())), Offer.class);
            if (request.isForm() && request.isOffer())
                mongoTemplate.remove(Query.query(Criteria.where("_id").is(request.formId())), OfferForm.class);
            if (request.isForm() && request.isOffer() && request.isPurchasedOffers())
                mongoTemplate.remove(Query.query(Criteria.where("formId").is(request.formId())), PurchasedOffer.class);

            return OfferGeneralResponse.builder()
                    .offerId(request.offerId())
                    .status("Deleted successfully")
                    .build();
        } catch (Exception e) {
            return OfferGeneralResponse.builder()
                    .offerId(request.offerId())
                    .status("Deletion failed")
                    .build();
        }
    }

    public OfferGeneralResponse updateStatus(OfferStateRequest request) {
        var query = new Query(Criteria.where("_id").is(request.offerId()));
        var update = new Update();
        update.set("isActive", request.state());
        Offer updatedOffer = mongoTemplate.findAndModify(query, update, Offer.class);
        return OfferGeneralResponse.builder()
                .offerId(request.offerId())
                .status((updatedOffer != null) ? "Updated successfully" : "Deletion failed")
                .build();

    }

    public OfferResponse getByOfferId(String offerId) {
        var offer=offerRepository.findById(offerId).orElse(null);
        return OfferMapper.toDto(offer);
    }
}