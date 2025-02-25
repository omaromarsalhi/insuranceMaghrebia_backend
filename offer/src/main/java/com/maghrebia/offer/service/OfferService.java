package com.maghrebia.offer.service;

import com.maghrebia.offer.dto.OfferRequest;
import com.maghrebia.offer.mapper.OfferMapper;
import com.maghrebia.offer.model.Offer;
import com.maghrebia.offer.repository.OfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



@AllArgsConstructor
@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;


    public Offer createOffer(OfferRequest offer) {
        return offerRepository.save(OfferMapper.toEntity(offer));
    }




}