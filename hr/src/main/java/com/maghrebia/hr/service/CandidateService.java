package com.maghrebia.hr.service;

import com.maghrebia.hr.dto.request.CandidateRequest;
import com.maghrebia.hr.entity.*;
import com.maghrebia.hr.exception.CandidateNotFoundException;
import com.maghrebia.hr.exception.JobNotFoundException;
import com.maghrebia.hr.repository.CandidateRepository;
import com.maghrebia.hr.repository.InterviewRepository;
import com.maghrebia.hr.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final JobPostingRepository jobPostingRepository;
    private static final String UPLOAD_DIR = "uploads/candidates/";
    private final InterviewRepository interviewRepository;


    public Candidate createCandidate(CandidateRequest candidateRequest, String jobId) throws IOException {
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("The id of this posting was not found"));

        String resumePath = saveFile(candidateRequest.getResume(), candidateRequest.getFirstname() + candidateRequest.getLastname() + "_resume");
        String coverLetterPath = saveFile(candidateRequest.getCoverLetter(), candidateRequest.getFirstname() + candidateRequest.getLastname() + "_coverLetter");

        Candidate candidate = Candidate.builder()
                .firstname(candidateRequest.getFirstname())
                .lastname(candidateRequest.getLastname())
                .email(candidateRequest.getEmail())
                .resume(resumePath)
                .coverLetter(coverLetterPath)
                .status(CandidateStatus.PENDING)
                .build();
        Candidate candi = candidateRepository.save(candidate);
        List<Candidate> existingCandidates = job.getCandidates();

        if (existingCandidates == null) {
            existingCandidates = new ArrayList<>();
        }
        existingCandidates.add(candi);
        job.setCandidates(existingCandidates);
        jobPostingRepository.save(job);
        return candi;
    }

    private String saveFile(MultipartFile file, String name) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) directory.mkdirs();
        String fileName = name + "_" + UUID.randomUUID().toString().substring(0, 8) + ".pdf";
        String filePath = UPLOAD_DIR + fileName;
        Files.write(Paths.get(filePath), file.getBytes());
        return filePath;
    }

    public List<Candidate> findAll() {
        return candidateRepository.findAll();
    }

    public Candidate findCandidate(String id) {
        return candidateRepository.findById(id).orElseThrow(() -> new CandidateNotFoundException("This id of candidate not found"));
    }

    public Candidate reject(String id) {
        Candidate candidate = findCandidate(id);
        if(candidate.getStatus()== CandidateStatus.INTERVIEW_SCHEDULED) {
            Interview interview = interviewRepository.findInterviewByCandidateId(candidate);
            interview.setStatus(InterviewStatus.CANCELLED);
            interviewRepository.save(interview);
        }
        candidate.setStatus(CandidateStatus.REJECTED);
        return candidateRepository.save(candidate);
    }
    public Candidate hire(String id) {
        Candidate candidate = findCandidate(id);
            Interview interview = interviewRepository.findInterviewByCandidateId(candidate);
            interview.setStatus(InterviewStatus.COMPLETED);
            interviewRepository.save(interview);
        candidate.setStatus(CandidateStatus.HIRED);
        return candidateRepository.save(candidate);
    }
}
