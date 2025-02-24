package com.maghrebia.complaint.service;

import com.maghrebia.complaint.entity.Complaint;
import com.maghrebia.complaint.entity.ComplaintType;
import com.maghrebia.complaint.entity.StatusComplaint;
import com.maghrebia.complaint.exception.ComplaintNotFoundException;
import com.maghrebia.complaint.exception.InvalidComplaintException;
import com.maghrebia.complaint.exception.UserNotFoundException;
import com.maghrebia.complaint.repository.ComplaintRepository;
import com.maghrebia.complaint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final AiService aiService;


    public Complaint addComplaint(Complaint complaint, String userId) {
        try {
            if (!StringUtils.hasText(userId)) {
                throw new IllegalArgumentException("User ID cannot be null/empty");
            }

            if (userRepository.findById(userId)==null) {
                throw new UserNotFoundException("User not found with ID: " + userId);
            }

            if (complaint == null || !StringUtils.hasText(complaint.getComplaintDescription())) {
                throw new InvalidComplaintException("Complaint description is required");
            }
            if (!aiService.isComplaintValid(complaint.getTitle(), complaint.getComplaintDescription())) {
                String responseMessage = aiService.getComplaintResponse(complaint.getTitle(), complaint.getComplaintDescription());
                throw new InvalidComplaintException("Complaint title and description are not compatible"+responseMessage);
            }
            complaint.setCreatedAt(LocalDateTime.now());
            complaint.setUserId(userId);
            complaint.setComplaintStatus(StatusComplaint.CLOSED);

            return complaintRepository.save(complaint);

        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while saving complaint", e);
        }
    }

    public void deleteComplaint(String complaintId) {
        try {
            if (!complaintRepository.existsById(complaintId)) {
                throw new ComplaintNotFoundException("Complaint not found with ID: " + complaintId);
            }
            complaintRepository.deleteById(complaintId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while deleting complaint", e);
        }
    }

    public List<Complaint> getAllComplaints() {
        try {
            return complaintRepository.findAll();
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while fetching complaints", e);
        }
    }

    public Complaint getComplaintById(String id) {
        try {
            return complaintRepository.findById(id)
                    .orElseThrow(() -> new ComplaintNotFoundException("Complaint not found with ID: " + id));
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while fetching complaint", e);
        }
    }

    public List<Complaint> getComplaintByUserId(String userId) {
        try {
            if (!userRepository.existsById(userId)) {
                throw new UserNotFoundException("User not found with ID: " + userId);
            }

            List<Complaint> complaints = complaintRepository.findAllByUserId(userId);

            if (complaints.isEmpty()) {
                throw new ComplaintNotFoundException("No complaints found for user ID: " + userId);
            }

            return complaints;
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while fetching user complaints", e);
        }
    }

    public Complaint updateComplaint(Complaint complaint) {
        try {
            if (complaint == null || !StringUtils.hasText(complaint.getComplaintId())) {
                throw new InvalidComplaintException("Invalid complaint data for update");
            }

            if (!complaintRepository.existsById(complaint.getComplaintId())) {
                throw new ComplaintNotFoundException("Complaint not found with ID: " + complaint.getComplaintId());
            }

            return complaintRepository.save(complaint);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while updating complaint", e);
        }
    }

    public List<Complaint> getComplaintsByType(ComplaintType complaintType) {
        try {
            List<Complaint> complaints = complaintRepository.findAllByComplaintType(complaintType);

            if (complaints.isEmpty()) {
                throw new ComplaintNotFoundException("No complaints found for type: " + complaintType);
            }

            return complaints;
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while fetching complaints by type", e);
        }
    }


}