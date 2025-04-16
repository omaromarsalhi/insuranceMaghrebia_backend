package com.maghrebia.hr.repository;

import com.maghrebia.hr.entity.Candidate;
import com.maghrebia.hr.entity.JobPosting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends MongoRepository<Candidate, String> {
}
