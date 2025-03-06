package com.maghrebia.offer.repository;

import com.maghrebia.offer.model.PurchasedOffer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PurchasedOfferRepository extends MongoRepository<PurchasedOffer, String> {
}
