package com.maghrebia.hr.controller;

import com.maghrebia.hr.dto.request.CandidateRequest;
import com.maghrebia.hr.entity.Candidate;
import com.maghrebia.hr.service.CandidateService;
import com.maghrebia.hr.service.FastApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("candidate")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;
    private final FastApiClient fastApiClient;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCandidate(@RequestParam("firstname") String firstname,
                                          @RequestParam("lastname") String lastname,
                                          @RequestParam("email") String email,
                                          @RequestParam("resume") MultipartFile resume,
                                          @RequestParam("coverLetter") MultipartFile coverLetter,
                                          @RequestParam String jobId) throws IOException {
        CandidateRequest candidateRequest = new CandidateRequest();
        candidateRequest.setFirstname(firstname);
        candidateRequest.setLastname(lastname);
        candidateRequest.setEmail(email);
        candidateRequest.setResume(resume);
        candidateRequest.setCoverLetter(coverLetter);
        candidateService.createCandidate(candidateRequest, jobId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCandidates() {
        return ResponseEntity.ok(candidateService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCandidateById(@PathVariable String id) {
        return ResponseEntity.ok(candidateService.findCandidate(id));
    }

    @PostMapping("/reject")
    public ResponseEntity<?> rejectCandidate(@RequestParam String id) {
        return ResponseEntity.ok(candidateService.reject(id));
    }

    @PostMapping("/hire")
    public ResponseEntity<?> hireCandidate(@RequestParam String id) {
        return ResponseEntity.ok(candidateService.hire(id));
    }

}
