package com.maghrebia.user.repository;

import com.maghrebia.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
     boolean existsByEmail(String email);

}
