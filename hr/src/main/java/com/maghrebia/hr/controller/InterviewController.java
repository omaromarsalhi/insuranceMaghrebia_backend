package com.maghrebia.hr.controller;

import com.maghrebia.hr.dto.request.InterviewRequest;
import com.maghrebia.hr.entity.Interview;
import com.maghrebia.hr.service.InterviewService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("interview")
@RequiredArgsConstructor
public class InterviewController {
    private final InterviewService interviewService;

    @PostMapping("/add")
    public ResponseEntity<?> addInterview(@RequestBody InterviewRequest interviewRequest, @RequestParam String candidateId) throws MessagingException {
        return ResponseEntity.ok(interviewService.createInterview(interviewRequest, candidateId));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllInterviews() {
        return ResponseEntity.ok(interviewService.findAllInterviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInterviewById(@PathVariable String id) {
        return ResponseEntity.ok(interviewService.findInterviewById(id));
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelInterview(@RequestParam String id) {
        return ResponseEntity.ok(interviewService.cancel(id));
    }
}
