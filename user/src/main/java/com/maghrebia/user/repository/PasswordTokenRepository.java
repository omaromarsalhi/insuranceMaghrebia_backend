package com.maghrebia.user.repository;

import com.maghrebia.user.entity.PasswordToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PasswordTokenRepository extends MongoRepository<PasswordToken,String> {
    Optional<PasswordToken> findByToken(String token);
}
