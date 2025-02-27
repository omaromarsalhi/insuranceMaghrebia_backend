package com.maghrebia.offer.service;

import com.maghrebia.offer.dto.OfferFormRequest;
import com.maghrebia.offer.dto.OfferFormResponse;
import com.maghrebia.offer.dto.OfferRequest;
import com.maghrebia.offer.dto.OfferResponse;
import com.maghrebia.offer.mapper.OfferFormMapper;
import com.maghrebia.offer.mapper.OfferMapper;
import com.maghrebia.offer.model.OfferForm;
import com.maghrebia.offer.repository.OfferFormRepository;
import com.maghrebia.offer.repository.OfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class OfferFormService {


    private final OfferFormRepository offerFormRepository;

    public OfferFormResponse create(OfferFormRequest request) {
        var savedForm = offerFormRepository.save(OfferFormMapper.toEntity(request));
        return OfferFormResponse
                .builder()
                .formId(savedForm.getFormId())
                .build();
    }




}