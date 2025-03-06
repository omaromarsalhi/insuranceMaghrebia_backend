package com.maghrebia.hr.service;

import com.maghrebia.hr.dto.request.InterviewRequest;
import com.maghrebia.hr.entity.Candidate;
import com.maghrebia.hr.entity.CandidateStatus;
import com.maghrebia.hr.entity.Interview;
import com.maghrebia.hr.entity.InterviewStatus;
import com.maghrebia.hr.exception.CandidateNotFoundException;
import com.maghrebia.hr.exception.InterviewNotFoundException;
import com.maghrebia.hr.repository.CandidateRepository;
import com.maghrebia.hr.repository.InterviewRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository interviewRepository;
    private final CandidateRepository candidateRepository;
    private final EmailService emailService;

    public Interview createInterview(InterviewRequest interviewRequest, String candidateId) throws MessagingException {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(() -> new CandidateNotFoundException("The id of candidate not found"));
        Interview interview = Interview.builder()
                .location(interviewRequest.getLocation())
                .scheduledDate(interviewRequest.getScheduledDate())
                .status(InterviewStatus.SCHEDULED)
                .candidateId(candidate)
                .build();
        candidate.setStatus(CandidateStatus.INTERVIEW_SCHEDULED);
        candidateRepository.save(candidate);
        String candidateName = candidate.getFirstname() + " " + candidate.getLastname();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy");
        emailService.sendInterviewTimeEmail(candidate.getEmail(), candidateName, "schedule-interview",
                interview.getScheduledDate().format(formatter), interview.getLocation(), "Interview Schedule - Maghrebia");
        return interviewRepository.save(interview);
    }

    public List<Interview> findAllInterviews() {
        return interviewRepository.findAllSorted();
    }

    public Interview findInterviewById(String id) {
        return interviewRepository.findById(id).orElseThrow(() -> new InterviewNotFoundException("The id of the interview not found"));
    }

    public Interview cancel(String id) {
        Interview interview = findInterviewById(id);
        interview.setStatus(InterviewStatus.CANCELLED);
        Candidate candidate=interview.getCandidateId();
        candidate.setStatus(CandidateStatus.REJECTED);
        candidateRepository.save(candidate);
        return interviewRepository.save(interview);
    }
}
