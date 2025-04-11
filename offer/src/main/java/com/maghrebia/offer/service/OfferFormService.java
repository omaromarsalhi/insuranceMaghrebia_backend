package com.maghrebia.offer.service;

import com.maghrebia.offer.dto.*;
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
        return OfferFormMapper.toResponse(savedForm);
    }

    public OfferFormResponse update(OfferFormUpdateRequest request) {
        var savedForm = offerFormRepository.save(OfferFormMapper.toUpdateEntity(request));
        return OfferFormMapper.toResponse(savedForm);
    }


    public OfferFormResponse getById(String formId) {
        var form = offerFormRepository.findById(formId).orElse(null);
        System.out.println(formId);
        System.out.println(form);
        return OfferFormMapper.toResponse(form);
    }
}