package com.maghrebia.claim.repository;

import com.maghrebia.claim.entity.IncidentType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IncidentTypeRepository extends MongoRepository<IncidentType, String> {

}
