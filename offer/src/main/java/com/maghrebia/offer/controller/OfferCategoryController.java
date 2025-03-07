package com.maghrebia.offer.controller;


import com.maghrebia.offer.dto.CategoryRequest;
import com.maghrebia.offer.dto.CategoryResponse;
import com.maghrebia.offer.model.OfferCategory;
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
    public ResponseEntity<CategoryResponse> createOfferCategory(@RequestBody CategoryRequest offerCategory) {
        return ResponseEntity.ok(offerCategoryService.createOfferCategory(offerCategory));
    }

    @GetMapping("getAll")
    public ResponseEntity<List<CategoryResponse>> getAllOfferCategories() {
        return ResponseEntity.ok(offerCategoryService.getAllOfferCategories());
    }

    @GetMapping("getOne/{id}")
    public ResponseEntity<CategoryResponse> getOfferCategoryById(@PathVariable String id) {
        return ResponseEntity.ok(offerCategoryService.getOfferCategoryById(id));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<CategoryResponse> updateOfferCategory(@PathVariable String id, @RequestBody OfferCategory offerCategoryDetails) {
        return ResponseEntity.ok(offerCategoryService.updateOfferCategory(id, offerCategoryDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOfferCategory(@PathVariable String id) {
        offerCategoryService.deleteOfferCategory(id);
        return ResponseEntity.noContent().build();
    }
}
