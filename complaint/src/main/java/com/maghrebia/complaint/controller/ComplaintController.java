package com.maghrebia.complaint.controller;

import com.maghrebia.complaint.entity.Complaint;
import com.maghrebia.complaint.entity.ComplaintType;
import com.maghrebia.complaint.entity.StatusComplaint;
import com.maghrebia.complaint.exception.InvalidComplaintException;
import com.maghrebia.complaint.exception.UserNotFoundException;
import com.maghrebia.complaint.service.AiService;
import com.maghrebia.complaint.service.ComplaintService;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/complaint")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4300/, http://localhost:4200/ ")
public class ComplaintController {

    private final ComplaintService complaintService;
    private final AiService aiService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> addComplaint(@Valid @RequestBody Complaint complaint,
                                          @PathVariable String userId,
                                          BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );

            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Complaint savedComplaint = complaintService.addComplaint(complaint, userId);
            return ResponseEntity.ok(savedComplaint);
        } catch (InvalidComplaintException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "An error occurred while saving the complaint."));
        }
    }


    @GetMapping("")
    public ResponseEntity<List<Complaint>> getAll(
    ) {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Complaint> getById(@PathVariable String id
    ) {
        return ResponseEntity.ok(complaintService.getComplaintById(id));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Complaint>> getComplaintByUserId(@PathVariable String userId
    ) {
        return ResponseEntity.ok(complaintService.getComplaintByUserId(userId));
    }

    @GetMapping("/getType/{type}")
    public ResponseEntity<List<Complaint>> getResponsesByType(@PathVariable ComplaintType type
    ) {
        return ResponseEntity.ok(complaintService.getComplaintsByType(type));
    }
    @PostMapping("/getTitle")
    public ResponseEntity<String> getSuggestedTitle(@RequestBody Map<String, String> requestBody) {

        String description = requestBody.get("description");
        if (description == null || description.isEmpty()) {
            return ResponseEntity.badRequest().body("No description provided.");
        }
        String suggestedTitle = aiService.getSuggestedTitle(description);

        if ("Error occurred while calling the Flask API.".equals(suggestedTitle)) {
            System.err.println(suggestedTitle);
            return ResponseEntity.status(500).body(suggestedTitle);
        }

        return ResponseEntity.ok(suggestedTitle);
    }

    @PutMapping("/{idComplaint}/{status}")
    public ResponseEntity<String> updateStatus(@PathVariable String idComplaint, @PathVariable StatusComplaint status) {
        complaintService.updateStatus(idComplaint, status);
        return ResponseEntity.ok("Status updated successfully!");
    }
    @PostMapping("")
    public ResponseEntity<?> test() {
        System.out.println("hihihihi");
        return ResponseEntity.ok(Collections.singletonMap("message", "Status updated successfully!"));
    }

}
