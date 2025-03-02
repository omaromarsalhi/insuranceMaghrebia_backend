package com.maghrebia.hr.repository;

import com.maghrebia.hr.entity.Interview;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends MongoRepository<Interview, String> {

}
