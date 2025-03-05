package com.maghrebia.hr.service;

import com.maghrebia.hr.dto.request.CandidateRequest;
import com.maghrebia.hr.entity.Candidate;
import com.maghrebia.hr.entity.CandidateStatus;
import com.maghrebia.hr.entity.JobPosting;
import com.maghrebia.hr.exception.CandidateNotFoundException;
import com.maghrebia.hr.exception.JobNotFoundException;
import com.maghrebia.hr.repository.CandidateRepository;
import com.maghrebia.hr.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final JobPostingRepository jobPostingRepository;

    public Candidate createCandidate(CandidateRequest candidateRequest, String jobId) {
        JobPosting job = jobPostingRepository.findById(jobId).orElseThrow(() -> new JobNotFoundException("The id of this posting was not found"));
        Candidate candidate = Candidate.builder()
                .firstname(candidateRequest.getFirstname())
                .lastname(candidateRequest.getLastname())
                .email(candidateRequest.getEmail())
                .resume(candidateRequest.getResume())
                .coverLetter(candidateRequest.getCoverLetter())
                .status(CandidateStatus.PENDING)
                .appliedJobId(job)
                .build();
        return candidateRepository.save(candidate);
    }

    public List<Candidate> findAllCandidate() {
        return candidateRepository.findAll();
    }

    public Candidate findCandidate(String id) {
        return candidateRepository.findById(id).orElseThrow(() -> new CandidateNotFoundException("This is of candidate not found"));
    }
}
