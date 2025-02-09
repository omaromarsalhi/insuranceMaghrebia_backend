package com.maghrebia.complaint.service;

import com.maghrebia.complaint.entity.Complaint;
import com.maghrebia.complaint.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ComplaintService {


    private  final ComplaintRepository complaintRepository;

    public Complaint addComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
    }
}
