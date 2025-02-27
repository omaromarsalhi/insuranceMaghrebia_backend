package com.maghrebia.useraction.Repository;

import com.maghrebia.useraction.entity.UserAction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TrackingRepository extends MongoRepository<UserAction,String> {
    Optional<UserAction> findByuserId(String userId);
}
