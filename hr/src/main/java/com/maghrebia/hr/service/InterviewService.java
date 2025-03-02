package com.maghrebia.hr.service;

import com.maghrebia.hr.dto.request.InterviewRequest;
import com.maghrebia.hr.entity.Candidate;
import com.maghrebia.hr.entity.Interview;
import com.maghrebia.hr.entity.InterviewStatus;
import com.maghrebia.hr.exception.CandidateNotFoundException;
import com.maghrebia.hr.exception.InterviewNotFoundException;
import com.maghrebia.hr.repository.CandidateRepository;
import com.maghrebia.hr.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository interviewRepository;
    private final CandidateRepository candidateRepository;

    public Interview createInterview(InterviewRequest interviewRequest, String candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(() -> new CandidateNotFoundException("The id of candidate not found"));
        Interview interview = Interview.builder()
                .location(interviewRequest.getLocation())
                .scheduledDate(interviewRequest.getScheduledDate())
                .status(InterviewStatus.SCHEDULED)
                .candidateId(candidate)
                .build();
        return interviewRepository.save(interview);
    }

    public List<Interview> findAllInterviews() {
        return interviewRepository.findAll();
    }
    public Interview findInterviewById(String id) {
        return interviewRepository.findById(id).orElseThrow(()-> new InterviewNotFoundException("The id of the interview not found"));
    }
}
