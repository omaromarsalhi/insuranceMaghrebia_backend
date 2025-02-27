package com.maghrebia.offer.service;

import com.maghrebia.offer.dto.OfferRequest;
import com.maghrebia.offer.dto.OfferResponse;
import com.maghrebia.offer.mapper.OfferMapper;
import com.maghrebia.offer.model.Offer;
import com.maghrebia.offer.repository.OfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@AllArgsConstructor
@Service
public class OfferService {

    private final MongoTemplate mongoTemplate;
    private final OfferRepository offerRepository;


    public OfferResponse create(OfferRequest offer) {
        var savedOffer = offerRepository.save(OfferMapper.toEntity(offer));
        return OfferResponse.builder()
                .offerId(savedOffer.getOfferId())
                .name(savedOffer.getName())
                .header(savedOffer.getHeader())
                .category(offer.category())
                .imageUri(offer.imageUri())
                .labels(offer.labels())
                .build();
    }


    @Transactional
    public void addFormToOffer(String offerId,String formId) {
        Query query = new Query(Criteria.where("_id").is(offerId));
        Update update = new Update().set("form", formId);
        mongoTemplate.updateFirst(query, update, Offer.class);
    }



}