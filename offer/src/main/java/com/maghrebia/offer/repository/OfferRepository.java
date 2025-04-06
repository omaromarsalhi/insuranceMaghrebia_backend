package com.maghrebia.offer.repository;

import com.maghrebia.offer.model.Offer;

import com.maghrebia.offer.model.OfferCategory;
import com.maghrebia.offer.model.records.FilteredCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface OfferRepository extends MongoRepository<Offer, String> {

    Offer findOneByCategory(FilteredCategory category);

    List<Offer> findAllByCategory_CategoryId(String categoryCategoryId);
}