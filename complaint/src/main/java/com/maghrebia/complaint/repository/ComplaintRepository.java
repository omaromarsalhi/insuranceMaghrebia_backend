package com.maghrebia.complaint.repository;

import com.maghrebia.complaint.entity.Complaint;
import com.maghrebia.complaint.entity.ComplaintType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends MongoRepository<Complaint,String>  {
    List<Complaint> findAllByComplaintType(ComplaintType complaintType);
    List<Complaint> findAllByUserId(String userId);
}
