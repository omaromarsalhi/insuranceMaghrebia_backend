package com.maghrebia.complaint.controller;

import com.maghrebia.complaint.entity.Complaint;
import com.maghrebia.complaint.repository.ComplaintRepository;
import com.maghrebia.complaint.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/complaint")
@RequiredArgsConstructor
public class ComplaintController {

    private  final ComplaintService complaintService;

    @PostMapping("")
    public ResponseEntity<Complaint> addComplaint(@RequestBody Complaint complaint
    ){
        System.out.println(complaint.getDescription());
        return ResponseEntity.ok(complaintService.addComplaint(complaint));
    }
}
