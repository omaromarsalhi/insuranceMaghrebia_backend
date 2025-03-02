package com.maghrebia.hr.controller;

import com.maghrebia.hr.dto.request.CandidateRequest;
import com.maghrebia.hr.entity.Candidate;
import com.maghrebia.hr.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("candidate")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;

    @PostMapping("/add")
    public ResponseEntity<?> addCandidate(@RequestBody CandidateRequest candidate, @RequestParam String jobId) {
        return ResponseEntity.ok(candidateService.createCandidate(candidate, jobId));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCandidates() {
        return ResponseEntity.ok(candidateService.findAllCandidate());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCandidateById(@PathVariable String id) {
        return ResponseEntity.ok(candidateService.findCandidate(id));
    }
}
