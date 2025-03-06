package com.maghrebia.offer.controller;


import com.maghrebia.offer.dto.OfferFormRequest;
import com.maghrebia.offer.dto.OfferFormResponse;
import com.maghrebia.offer.dto.PurchasedOfferRequest;
import com.maghrebia.offer.service.PurchasedOfferService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/purchased_offers")
public class PurchasedOfferController {

    private final PurchasedOfferService purchasedOfferService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody PurchasedOfferRequest request) {
        return ResponseEntity.ok(purchasedOfferService.create(request));
    }
}
