package com.maghrebia.offer.controller;


import com.maghrebia.offer.dto.OfferFormRequest;
import com.maghrebia.offer.dto.OfferFormResponse;
import com.maghrebia.offer.dto.OfferRequest;
import com.maghrebia.offer.dto.OfferResponse;
import com.maghrebia.offer.service.OfferFormService;
import com.maghrebia.offer.service.OfferService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/offer_forms")
public class OfferFormController {


    private final OfferFormService offerFormService;

    @PostMapping("/create")
    public ResponseEntity<OfferFormResponse> create(@RequestBody @Valid OfferFormRequest request) {
        return ResponseEntity.ok(offerFormService.create(request));
    }


}