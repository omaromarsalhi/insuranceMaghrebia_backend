package com.maghrebia.offer.repository;

import com.maghrebia.offer.model.Offer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OfferRepository extends MongoRepository<Offer, String> {
}