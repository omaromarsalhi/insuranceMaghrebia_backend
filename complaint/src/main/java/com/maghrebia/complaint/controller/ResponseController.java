package com.maghrebia.complaint.controller;

import com.maghrebia.complaint.entity.Complaint;
import com.maghrebia.complaint.entity.ResponseComplaint;
import com.maghrebia.complaint.service.ComplaintService;
import com.maghrebia.complaint.service.ResponseService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/complaintResponse")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ResponseController {

    private  final ResponseService responseService;

    @PostMapping("/{responderId}/{complaintId}")
    public ResponseEntity<?> addResponse(@RequestBody ResponseComplaint responseComplaint,
                                                 @PathVariable String responderId,
                                                 @PathVariable String complaintId,
                                                 BindingResult result
    ){
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
                System.out.println("Validation error: " + error.getField() + " - " + error.getDefaultMessage()); // Log des erreurs
            });
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(responseService.addRespons(responseComplaint,responderId,complaintId));
    }

    @GetMapping("/{complaintId}")
    public ResponseEntity<List<ResponseComplaint>> getResponsesByComplainId(
            @PathVariable String complaintId
    ){
        return ResponseEntity.ok(responseService.getResponsesByComplainId(complaintId));
    }


    @DeleteMapping("")
    public Response deleteResponse(@RequestBody ResponseComplaint responseComplaint
    ){
        responseService.deleteResponse(responseComplaint);
        return Response.ok().build();
    }

}
