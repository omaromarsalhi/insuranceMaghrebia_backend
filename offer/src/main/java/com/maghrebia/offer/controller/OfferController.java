package com.maghrebia.offer.controller;


import com.maghrebia.offer.dto.OfferRequest;
import com.maghrebia.offer.dto.OfferResponse;
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

    @PostMapping("create")
    public ResponseEntity<OfferResponse> create(@RequestBody  OfferRequest request) {
        return ResponseEntity.ok(offerService.create(request));
    }

    @GetMapping("/one")
    public ResponseEntity<OfferResponse> getOne() {
        return ResponseEntity.ok(offerService.getOne());
    }


}