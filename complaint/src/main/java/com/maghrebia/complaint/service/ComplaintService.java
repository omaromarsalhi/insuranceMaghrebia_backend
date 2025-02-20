package com.maghrebia.complaint.service;

import com.maghrebia.complaint.entity.Complaint;
import com.maghrebia.complaint.entity.ComplaintType;
import com.maghrebia.complaint.entity.StatusComplaint;
import com.maghrebia.complaint.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ComplaintService {


    private  final ComplaintRepository complaintRepository;

    public Complaint addComplaint(Complaint complaint,String userId) {
        complaint.setCreatedAt(LocalDateTime.now());
        complaint.setUserId(userId);
        complaint.setComplaintStatus(StatusComplaint.CLOSED);
        return complaintRepository.save(complaint);
    }

    public void deleteComplaint (Complaint complaint) {
        complaintRepository.delete(complaint);
    }
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }
    public Complaint getComplaintById(String id) {
        return  complaintRepository.findById(id).get();
    }

    public List<Complaint> getComplaintByUserId(String userId) {
        return  complaintRepository.findAllByUserId(userId);
    }
    public Complaint updateComplaint(Complaint complaint) {
       return complaintRepository.save(complaint);

    }
    public List<Complaint> getResponsesByType(ComplaintType complaintType) {
        return  complaintRepository.findAllByComplaintType(complaintType);
    }

}
