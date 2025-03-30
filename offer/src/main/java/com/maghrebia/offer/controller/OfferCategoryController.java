package com.maghrebia.offer.controller;


import com.maghrebia.offer.dto.CategoryRequest;
import com.maghrebia.offer.dto.CategoryResponse;
import com.maghrebia.offer.model.OfferCategory;
import com.maghrebia.offer.model.enums.CategoryTarget;
import com.maghrebia.offer.service.OfferCategoryService;
import jakarta.ws.rs.QueryParam;
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
    public ResponseEntity<List<CategoryResponse>> getAll() {
        return ResponseEntity.ok(offerCategoryService.getAll());
    }

    @GetMapping("getAllByTarget/{target}")
    public ResponseEntity<List<CategoryResponse>> getAllByTarget(@PathVariable CategoryTarget target) {
        return ResponseEntity.ok(offerCategoryService.getAllByTarget(target));
    }

    @GetMapping("getOne/{id}")
    public ResponseEntity<CategoryResponse> getOfferCategoryById(@PathVariable String id) {
        return ResponseEntity.ok(offerCategoryService.getOfferCategoryById(id));
    }

    @PutMapping("update")
    public ResponseEntity<CategoryResponse> updateOfferCategory(@RequestBody OfferCategory offerCategoryDetails) {
        return ResponseEntity.ok(offerCategoryService.updateOfferCategory( offerCategoryDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOfferCategory(@PathVariable String id) {
        offerCategoryService.deleteOfferCategory(id);
        return ResponseEntity.noContent().build();
    }
}
