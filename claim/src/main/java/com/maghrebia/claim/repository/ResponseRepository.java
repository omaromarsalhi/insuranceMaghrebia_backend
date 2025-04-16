package com.maghrebia.claim.repository;

import com.maghrebia.claim.entity.Response;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ResponseRepository extends MongoRepository<Response, String> {
}
