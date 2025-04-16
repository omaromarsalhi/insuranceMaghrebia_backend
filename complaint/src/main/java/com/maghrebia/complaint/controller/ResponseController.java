package com.maghrebia.complaint.controller;

import com.maghrebia.complaint.entity.Complaint;
import com.maghrebia.complaint.entity.ResponseComplaint;
import com.maghrebia.complaint.service.ComplaintService;
import com.maghrebia.complaint.service.ResponseService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/complaintResponse")
@CrossOrigin("http://localhost:4300/, http://localhost:4200/ ")
@RequiredArgsConstructor
public class ResponseController {

    private final ResponseService responseService;

    @PostMapping("/{responderId}/{complaintId}")
    public ResponseEntity<?> addResponse(@RequestBody ResponseComplaint responseComplaint,
                                         @PathVariable String responderId,
                                         @PathVariable String complaintId,
                                         BindingResult result) {
        try {
            if (result.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                result.getFieldErrors().forEach(error -> {
                    errors.put(error.getField(), error.getDefaultMessage());
                    System.out.println("Validation error: " + error.getField() + " - " + error.getDefaultMessage());
                });
                return ResponseEntity.badRequest().body(errors);
            }
            return ResponseEntity.ok(responseService.addRespons(responseComplaint, responderId, complaintId));
        } catch (Exception e) {
            System.err.println("Error adding response: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the response.");
        }
    }

    @GetMapping("/{complaintId}")
    public ResponseEntity<?> getResponsesByComplainId(@PathVariable String complaintId) {
        try {
            List<ResponseComplaint> responses = responseService.getResponsesByComplainId(complaintId);
            if (responses == null || responses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No responses found for the given complaintId: " + complaintId);
            }
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            System.err.println("Error fetching responses: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching responses.");
        }
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteResponse(@RequestBody ResponseComplaint responseComplaint) {
        try {
            responseService.deleteResponse(responseComplaint);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error deleting response: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the response.");
        }
    }

    @PutMapping("/{responseId}")
    public ResponseEntity<?> markResponseAsSeen(@PathVariable String responseId) {
        try {
            ResponseComplaint updatedResponse = responseService.isSeen(responseId);
            if (updatedResponse != null) {
                return ResponseEntity.ok(updatedResponse);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Error marking response as seen: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while marking the response as seen.");
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        try {
            List<ResponseComplaint> responses = responseService.getAllComplaints();
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            System.err.println("Error fetching all responses: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching all responses.");
        }
    }
}