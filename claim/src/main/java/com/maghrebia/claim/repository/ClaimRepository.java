package com.maghrebia.claim.repository;

import com.maghrebia.claim.entity.Claim;
import com.maghrebia.claim.entity.IncidentType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClaimRepository extends MongoRepository<Claim, String> {

}
