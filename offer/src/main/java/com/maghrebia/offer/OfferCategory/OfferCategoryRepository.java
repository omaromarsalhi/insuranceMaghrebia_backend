package com.maghrebia.offer.OfferCategory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferCategoryRepository extends MongoRepository<OfferCategory, String> {
}