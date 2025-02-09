package com.maghrebia.complaint.repository;

import com.maghrebia.complaint.entity.Complaint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends MongoRepository<Complaint,String>  {
}
