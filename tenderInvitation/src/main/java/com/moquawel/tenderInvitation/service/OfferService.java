package com.moquawel.tenderInvitation.service;


import com.moquawel.tenderInvitation.offer.Offer;
import com.moquawel.tenderInvitation.request.FilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final MongoTemplate mongoTemplate;

    public void removeExpiredOffers() {
        Query query = new Query();
        query.addCriteria(Criteria.where("myBdRecvEndDt").lt(LocalDateTime.now()));
        mongoTemplate.remove(query, Offer.class);
    }


    public List<Offer> findFilteredData(FilterRequest filter) {
        Query query = new Query();
        if (filter.bdRecvEndDt() != null) {
            query.addCriteria(Criteria.where("myBdRecvEndDt").lt(filter.bdRecvEndDt()));
        }
        if (filter.publicDt() != null) {
            query.addCriteria(Criteria.where("myPublicDt").gte(filter.publicDt()));
        }
        if (filter.category() != null) {
            query.addCriteria(Criteria.where("category").is(filter.category()));
        }
        return mongoTemplate.find(query, Offer.class);
    }
}
