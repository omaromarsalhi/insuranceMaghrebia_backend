package com.maghrebia.useraction.Repository;

import com.maghrebia.useraction.entity.UserAction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrackingRepository extends MongoRepository<UserAction,String> {
    Optional<UserAction> findByuserId(String userId);
    @Query("{ 'userId': ?0, 'actions.date': { $gt: ?1 } }")
    List<UserAction> findActionsByUserIdAndActionDateAfter(String userId, LocalDateTime date);
}
