package com.maghrebia.user.repository;

import com.maghrebia.user.entity.ActivationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ActivationTokenRepository extends MongoRepository<ActivationToken, String> {
    Optional<ActivationToken> findByToken(String token);
}
