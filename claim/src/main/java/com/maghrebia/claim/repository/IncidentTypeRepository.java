package com.maghrebia.claim.repository;

import com.maghrebia.claim.entity.IncidentType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IncidentTypeRepository extends MongoRepository<IncidentType, String> {

    List<IncidentType> findAllByActive(boolean active);
}
