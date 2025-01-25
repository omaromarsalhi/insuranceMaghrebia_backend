package com.moquawel.tenderInvitation.offer;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OfferRepository extends MongoRepository<Offer, String> {

}
