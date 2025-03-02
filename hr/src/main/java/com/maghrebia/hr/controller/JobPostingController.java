package com.maghrebia.hr.controller;

import com.maghrebia.hr.dto.request.JobRequest;
import com.maghrebia.hr.entity.JobPosting;
import com.maghrebia.hr.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("job-posting")
@RequiredArgsConstructor
public class JobPostingController {
    private final JobPostingService jobPostingService;

    @PostMapping("/add")
    public ResponseEntity<?> addJobPosting(@RequestBody JobRequest jobPosting) {
        return ResponseEntity.ok(jobPostingService.createJob(jobPosting));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllJobPostings() {
        return ResponseEntity.ok(jobPostingService.findAllJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobPostingById(@PathVariable String id) {
        return ResponseEntity.ok(jobPostingService.findJobById(id));
    }
}
