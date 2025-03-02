package com.maghrebia.offer.service;

import com.maghrebia.offer.dto.OfferRequest;
import com.maghrebia.offer.dto.OfferResponse;
import com.maghrebia.offer.mapper.OfferMapper;
import com.maghrebia.offer.model.Offer;
import com.maghrebia.offer.repository.OfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;




@AllArgsConstructor
@Service
public class OfferService {

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


    public OfferResponse getOne() {
        Offer offer=offerRepository.findAll().stream().findFirst().orElse(null);
        System.out.println(offer);
        assert offer != null;
        return OfferMapper.toDto(offer);
    }
}