package com.maghrebia.offer.service;

import com.maghrebia.offer.dto.CategoryRequest;
import com.maghrebia.offer.dto.CategoryResponse;
import com.maghrebia.offer.exception.EntityNotFoundException;
import com.maghrebia.offer.mapper.CategoryMapper;
import com.maghrebia.offer.model.OfferCategory;
import com.maghrebia.offer.model.enums.CategoryTarget;
import com.maghrebia.offer.repository.OfferCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class OfferCategoryService {

    private final OfferCategoryRepository offerCategoryRepository;


    public CategoryResponse createOfferCategory(CategoryRequest offerCategory) {
        var savedCategory = offerCategoryRepository.save(CategoryMapper.toOfferCategory(offerCategory));
        return CategoryMapper.toCategoryResponse(savedCategory);
    }

    public List<CategoryResponse> getAll() {
        var allCategories = offerCategoryRepository.findAll();
        System.out.println(allCategories);
        return allCategories.stream()
                .map(CategoryMapper::toCategoryResponse)
                .toList();
    }

    public List<CategoryResponse> getAllByTarget(CategoryTarget target) {
        var allCategories = offerCategoryRepository.findByCategoryTarget(target);
        return allCategories.stream()
                .map(CategoryMapper::toCategoryResponse)
                .toList();
    }

    public CategoryResponse getOfferCategoryById(String id) {
        var savedCategory = offerCategoryRepository.findByCategoryId(id);
        return CategoryMapper.toCategoryResponse(savedCategory);
    }

    public CategoryResponse updateOfferCategory(OfferCategory offerCategoryDetails) {
        OfferCategory offerCategory = offerCategoryRepository.findById(offerCategoryDetails.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("OfferCategory not found"));

        offerCategory.setName(offerCategoryDetails.getName());
        offerCategory.setDescription(offerCategoryDetails.getDescription());
        offerCategory.setCategoryTarget(offerCategoryDetails.getCategoryTarget());
        offerCategory.setImageUri(offerCategoryDetails.getImageUri());

        var savedCategory = offerCategoryRepository.save(offerCategory);
        return CategoryMapper.toCategoryResponse(savedCategory);
    }

    public void deleteOfferCategory(String id) {
        offerCategoryRepository.deleteById(id);
    }
}