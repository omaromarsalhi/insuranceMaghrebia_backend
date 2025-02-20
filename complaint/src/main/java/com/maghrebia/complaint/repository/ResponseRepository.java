package com.maghrebia.complaint.repository;

import com.maghrebia.complaint.entity.ComplaintType;
import com.maghrebia.complaint.entity.ResponseComplaint;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ResponseRepository extends MongoRepository<ResponseComplaint,String> {
    List<ResponseComplaint>  findAllByComplaintId(String complaintId);

}
