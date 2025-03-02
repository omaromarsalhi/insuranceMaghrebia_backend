package com.maghrebia.payment.repository;


import com.maghrebia.payment.entity.IndexTracker;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IndexTrackerRepository extends MongoRepository<IndexTracker, String> {
}
