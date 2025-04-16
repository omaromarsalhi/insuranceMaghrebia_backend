package com.maghrebia.claim.repository;

import com.maghrebia.claim.entity.Claim;
import com.maghrebia.claim.entity.IncidentType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClaimRepository extends MongoRepository<Claim, String> {
    List<Claim> findAllByUserId(String user_id);
}
