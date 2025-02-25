package com.maghrebia.offer.service;

import com.maghrebia.offer.model.OfferCategory;
import com.maghrebia.offer.repository.OfferCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class OfferCategoryService {

    private final OfferCategoryRepository offerCategoryRepository;


    public OfferCategory createOfferCategory(OfferCategory offerCategory) {
        return offerCategoryRepository.save(offerCategory);
    }

    public List<OfferCategory> getAllOfferCategories() {
        return offerCategoryRepository.findAll();
    }

    public OfferCategory getOfferCategoryById(String id) {
        return offerCategoryRepository.findById(id).orElse(null);
    }

    public OfferCategory updateOfferCategory(String id, OfferCategory offerCategoryDetails) {
        OfferCategory offerCategory = offerCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OfferCategory not found with id: " + id));

        offerCategory.setName(offerCategoryDetails.getName());
        offerCategory.setDescription(offerCategoryDetails.getDescription());
        offerCategory.setCategoryTarget(offerCategoryDetails.getCategoryTarget());

        return offerCategoryRepository.save(offerCategory);
    }

    public void deleteOfferCategory(String id) {
        offerCategoryRepository.deleteById(id);
    }
}