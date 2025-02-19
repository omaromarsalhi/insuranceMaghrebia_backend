package com.maghrebia.complaint.controller;

import com.maghrebia.complaint.entity.Complaint;
import com.maghrebia.complaint.entity.ComplaintType;
import com.maghrebia.complaint.service.ComplaintService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/complaint")
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = {"http://localhost:49773","http://localhost:4200"})
@RequiredArgsConstructor
public class ComplaintController {

    private  final ComplaintService complaintService;

    @PostMapping("/{userId}")
    public ResponseEntity<Complaint> addComplaint(@RequestBody Complaint complaint,@PathVariable String userId
    ){
        return ResponseEntity.ok(complaintService.addComplaint(complaint,userId));
    }

    @GetMapping("")
    public ResponseEntity<List<Complaint>> getAll(
    ){
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<Complaint> getById( @PathVariable String id
    ){
        return ResponseEntity.ok(complaintService.getComplaintById(id));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Complaint>> getComplaintByUserId( @PathVariable String userId
    ){
        return ResponseEntity.ok(complaintService.getComplaintByUserId(userId));
    }
    @GetMapping("/getType/{type}")
    public ResponseEntity<List<Complaint>> getResponsesByType( @PathVariable ComplaintType type
    ){
        return ResponseEntity.ok(complaintService.getResponsesByType(type));
    }
    @DeleteMapping("")
    public Response delete(@RequestBody Complaint complaint
    ){
        complaintService.deleteComplaint(complaint);
        return Response.ok().build();
    }

//    @PostMapping("")
//    public ResponseEntity<Complaint> updateComplaint(@RequestBody Complaint complaint
//    ){
//        return ResponseEntity.ok(complaintService.addComplaint(complaint));
//    }



}
