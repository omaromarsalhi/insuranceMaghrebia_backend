package com.maghrebia.complaint.repository;

import com.maghrebia.complaint.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
}
