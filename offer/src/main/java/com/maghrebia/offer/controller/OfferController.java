package com.maghrebia.offer.controller;


import com.maghrebia.offer.dto.OfferRequest;
import com.maghrebia.offer.model.Offer;
import com.maghrebia.offer.service.OfferService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/offers")
public class OfferController {

    private final OfferService offerService;

    @PostMapping
    public ResponseEntity<Offer> createOffer(@RequestBody @Valid OfferRequest offer) {
        System.out.println(offer);
        return ResponseEntity.ok(offerService.createOffer(offer));
    }


}