package com.maghrebia.offer.repository;

import com.maghrebia.offer.model.OfferForm;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OfferFormRepository  extends MongoRepository<OfferForm, String> {
    OfferForm findByFormId(String formId);
}
