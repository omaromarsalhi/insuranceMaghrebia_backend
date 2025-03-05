package com.maghrebia.hr.repository;

import com.maghrebia.hr.entity.JobPosting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends MongoRepository<JobPosting, String> {

    List<JobPosting> findAllByOrderByIsOpenDesc();

    List<JobPosting> findAllByIsOpen(Boolean isOpen);
}
