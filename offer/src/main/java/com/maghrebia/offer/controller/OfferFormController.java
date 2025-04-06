package com.maghrebia.offer.controller;


import com.maghrebia.offer.dto.*;
import com.maghrebia.offer.service.OfferFormService;
import com.maghrebia.offer.service.OfferService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/offer_forms")
public class OfferFormController {


    private final OfferFormService offerFormService;

    @PostMapping("/create")
    public ResponseEntity<OfferFormResponse> create(@RequestBody  OfferFormRequest request) {
        return ResponseEntity.ok(offerFormService.create(request));
    }

    @PutMapping("/update")
    public ResponseEntity<OfferFormResponse> update(@RequestBody OfferFormUpdateRequest request) {
        return ResponseEntity.ok(offerFormService.update(request));
    }

    @GetMapping("/{formId}")
    public ResponseEntity<OfferFormResponse> get(@PathVariable String formId) {
        return ResponseEntity.ok(offerFormService.getById(formId));
    }


}