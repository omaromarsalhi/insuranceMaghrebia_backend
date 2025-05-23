package com.maghrebia.offer.service;

import com.maghrebia.offer.dto.*;
import com.maghrebia.offer.exception.EntityNotFoundException;
import com.maghrebia.offer.mapper.OfferMapper;
import com.maghrebia.offer.model.Offer;
import com.maghrebia.offer.model.OfferForm;
import com.maghrebia.offer.model.PurchasedOffer;
import com.maghrebia.offer.model.records.FilteredCategory;
import com.maghrebia.offer.repository.OfferCategoryRepository;
import com.maghrebia.offer.repository.OfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final OfferCategoryService offerCategoryService;
    private final MongoTemplate mongoTemplate;
    private final OfferCategoryRepository offerCategoryRepository;


    public OfferResponse create(OfferRequest offer) {
        var savedOffer = offerRepository.save(OfferMapper.toEntity(offer));
        return OfferMapper.toDto(savedOffer);
    }

    public OfferResponse update(OfferUpdateRequest offer) {
        if (offerRepository.existsById(offer.offerId())) {
            var entity= OfferMapper.toUpdateEntity(offer);
            System.out.println(entity);
            var savedOffer = offerRepository.save(entity);
            return OfferMapper.toDto(savedOffer);
        } else throw new EntityNotFoundException("offer does not exist");
    }


    public List<OfferWithTagsResponse> getAllByCategoryId(String categoryId) {
        if (offerCategoryRepository.existsById(categoryId)) {
            var offerList = offerRepository.findAllByCategory_CategoryId(categoryId);
            return offerList.stream()
                    .filter(Offer::isActive)
                    .map(OfferMapper::toTagDto).toList();
        } else throw new EntityNotFoundException("category does not exist");
    }

    public List<OfferResponse> getAll() {
        var offer = offerRepository.findAll();
        return offer.stream()
                .map(OfferMapper::toDto)
                .toList();
    }

    public OfferGeneralResponse delete(OfferDeletionRequest request) {
        try {
            if (request.isOffer())
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
        var offer = offerRepository.findById(offerId).orElse(null);
        return OfferMapper.toDto(offer);
    }
}