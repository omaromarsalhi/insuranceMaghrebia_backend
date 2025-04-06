package com.maghrebia.offer.repository;

import com.maghrebia.offer.model.OfferCategory;
import com.maghrebia.offer.model.enums.CategoryTarget;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferCategoryRepository extends MongoRepository<OfferCategory, String> {
    OfferCategory findByCategoryId(String categoryId);

    List<OfferCategory> findByCategoryTarget(CategoryTarget categoryTarget);
}