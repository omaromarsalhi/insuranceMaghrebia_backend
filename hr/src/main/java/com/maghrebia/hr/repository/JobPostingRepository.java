package com.maghrebia.hr.repository;

import com.maghrebia.hr.entity.JobPosting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostingRepository extends MongoRepository<JobPosting, String> {
}
