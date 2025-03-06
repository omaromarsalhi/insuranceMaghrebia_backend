package com.maghrebia.offer.service;

import com.maghrebia.offer.dto.OfferRequest;
import com.maghrebia.offer.dto.OfferResponse;
import com.maghrebia.offer.dto.PurchasedOfferRequest;
import com.maghrebia.offer.mapper.OfferMapper;
import com.maghrebia.offer.mapper.PurchasedOfferMapper;
import com.maghrebia.offer.model.records.FilteredCategory;
import com.maghrebia.offer.repository.OfferRepository;
import com.maghrebia.offer.repository.PurchasedOfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class PurchasedOfferService {

    private final PurchasedOfferRepository purchasedOfferRepository;


    public String create(PurchasedOfferRequest request) {
         purchasedOfferRepository.save(PurchasedOfferMapper.toEntity(request));
        return "done";
    }


}