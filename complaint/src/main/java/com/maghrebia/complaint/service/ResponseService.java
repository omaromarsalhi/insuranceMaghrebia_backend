package com.maghrebia.complaint.service;

import com.maghrebia.complaint.entity.Complaint;
import com.maghrebia.complaint.entity.ComplaintType;
import com.maghrebia.complaint.entity.ResponseComplaint;
import com.maghrebia.complaint.entity.StatusComplaint;
import com.maghrebia.complaint.repository.ComplaintRepository;
import com.maghrebia.complaint.repository.ResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponseService {
    private  final ResponseRepository responseRepository;
    private  final ComplaintRepository complaintRepository;

    public ResponseComplaint addRespons(ResponseComplaint response, String responderId,String complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId).get();
        complaint.setComplaintStatus(StatusComplaint.OPEN);
        complaintRepository.save(complaint);
        response.setResponderId(responderId);//l id de user qui va repond
        response.setComplaintId(complaintId);
        response.setCreatedAt(LocalDateTime.now());
        return responseRepository.save(response);
    }
    public void deleteResponse (ResponseComplaint response) {
        responseRepository.delete(response);
    }



    public List<ResponseComplaint> getResponsesByComplainId(String complaintId) {
        return  responseRepository.findAllByComplaintId(complaintId);
    }

}
