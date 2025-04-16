package com.maghrebia.claim.repository;

import com.maghrebia.claim.entity.IncidentType;
import com.maghrebia.claim.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
