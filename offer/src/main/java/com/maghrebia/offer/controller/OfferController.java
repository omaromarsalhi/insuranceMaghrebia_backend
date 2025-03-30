package com.maghrebia.offer.controller;


import com.maghrebia.offer.dto.*;
import com.maghrebia.offer.service.OfferService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/offers")
public class OfferController {

    private final OfferService offerService;

    @PostMapping("/create")
    public ResponseEntity<OfferResponse> create(@RequestBody OfferRequest request) {
        return ResponseEntity.ok(offerService.create(request));
    }

    @PutMapping ("/update")
    public ResponseEntity<OfferResponse> update(@RequestBody OfferUpdateRequest request) {
        return ResponseEntity.ok(offerService.update(request));
    }

    @GetMapping("/getAllByCategoryId/{categoryId}")
    public ResponseEntity<List<OfferWithTagsResponse>> getAllByCategoryId(@PathVariable String categoryId) {
        return ResponseEntity.ok(offerService.getAllByCategoryId(categoryId));
    }


    @GetMapping("/offer/{offerId}")
    public ResponseEntity<OfferResponse> getByOfferId(@PathVariable String offerId) {
        return ResponseEntity.ok(offerService.getByOfferId(offerId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OfferResponse>> getAll() {
        return ResponseEntity.ok(offerService.getAll());
    }


    @DeleteMapping
    public ResponseEntity<OfferGeneralResponse> delete(@RequestBody OfferDeletionRequest request) {
        return ResponseEntity.ok(offerService.delete(request));
    }

    @PatchMapping
    public ResponseEntity<OfferGeneralResponse> updateStatus(@RequestBody OfferStateRequest request) {
        return ResponseEntity.ok(offerService.updateStatus(request));
    }

}