package com.maghrebia.offer.controller;


import com.maghrebia.offer.OfferCategory.OfferCategory;
import com.maghrebia.offer.service.OfferCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/offer-categories")
@RequiredArgsConstructor
public class OfferCategoryController {

    private final OfferCategoryService offerCategoryService;

    @PostMapping("/create")
    public OfferCategory createOfferCategory(@RequestBody OfferCategory offerCategory) {
        return offerCategoryService.createOfferCategory(offerCategory);
    }

    @GetMapping("getAll")
    public List<OfferCategory> getAllOfferCategories() {
        return offerCategoryService.getAllOfferCategories();
    }

    @GetMapping("getOne/{id}")
    public ResponseEntity<OfferCategory> getOfferCategoryById(@PathVariable String id) {
        return offerCategoryService.getOfferCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("update/{id}")
    public ResponseEntity<OfferCategory> updateOfferCategory(@PathVariable String id, @RequestBody OfferCategory offerCategoryDetails) {
        OfferCategory updatedOfferCategory = offerCategoryService.updateOfferCategory(id, offerCategoryDetails);
        return ResponseEntity.ok(updatedOfferCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOfferCategory(@PathVariable String id) {
        offerCategoryService.deleteOfferCategory(id);
        return ResponseEntity.noContent().build();
    }
}
