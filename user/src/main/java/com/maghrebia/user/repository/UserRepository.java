package com.maghrebia.user.repository;

import com.maghrebia.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
     boolean existsByEmail(String email);
    @Query("{ '_id' : { $ne: ?0 } }")
    List<User> findAllExceptById(String id);
}
