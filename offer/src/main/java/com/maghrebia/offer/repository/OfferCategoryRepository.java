package com.maghrebia.offer.repository;

import com.maghrebia.offer.model.OfferCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferCategoryRepository extends MongoRepository<OfferCategory, String> {
}