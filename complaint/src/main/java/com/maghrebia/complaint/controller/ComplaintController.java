package com.maghrebia.complaint.controller;

import com.maghrebia.complaint.entity.Complaint;
import com.maghrebia.complaint.entity.ComplaintType;
import com.maghrebia.complaint.service.ComplaintService;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/complaint")
@CrossOrigin(origins = {" http://localhost:54237 ","http://localhost:4200"})
@RequiredArgsConstructor
public class ComplaintController {

    private  final ComplaintService complaintService;
    @PostMapping("/{userId}")
    public ResponseEntity<?> addComplaint(@Valid @RequestBody Complaint complaint,
                                          @PathVariable String userId,
                                          BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
                System.out.println("Validation error: " + error.getField() + " - " + error.getDefaultMessage()); // Log des erreurs
            });
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(complaintService.addComplaint(complaint, userId));
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
