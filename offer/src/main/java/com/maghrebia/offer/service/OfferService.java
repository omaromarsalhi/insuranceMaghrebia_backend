package com.maghrebia.offer.service;

import com.maghrebia.offer.offer.Offer;
import com.maghrebia.offer.offer.OfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class OfferService {

    private final OfferRepository offerRepository;


    public Offer createOffer(Offer offer) {
        return offerRepository.save(offer);
    }


    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }


    public Optional<Offer> getOfferById(String id) {
        return offerRepository.findById(id);
    }


    public Offer updateOffer(String id, Offer offerDetails) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found with id: " + id));

        offer.setName(offerDetails.getName());
        offer.setDescription(offerDetails.getDescription());
        offer.setPremium(offerDetails.getPremium());
        offer.setValidity(offerDetails.getValidity());
        offer.setActive(offerDetails.isActive());
        offer.setOfferCategory(offerDetails.getOfferCategory());

        return offerRepository.save(offer);
    }

    public void deleteOffer(String id) {
        offerRepository.deleteById(id);
    }
}