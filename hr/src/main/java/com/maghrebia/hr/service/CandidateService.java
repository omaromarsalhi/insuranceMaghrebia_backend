package com.maghrebia.hr.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.maghrebia.hr.dto.request.CandidateRequest;
import com.maghrebia.hr.dto.request.ModelRequest;
import com.maghrebia.hr.entity.*;
import com.maghrebia.hr.exception.CandidateNotFoundException;
import com.maghrebia.hr.exception.JobNotFoundException;
import com.maghrebia.hr.repository.CandidateRepository;
import com.maghrebia.hr.repository.InterviewRepository;
import com.maghrebia.hr.repository.JobPostingRepository;
import com.maghrebia.hr.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final JobPostingRepository jobPostingRepository;
    private static final String UPLOAD_DIR = "uploads/candidates/";
    private final InterviewRepository interviewRepository;
    private final FastApiClient fastApiClient;

    public void createCandidate(CandidateRequest candidateRequest, String jobId) throws IOException {
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("The id of this posting was not found"));


        Candidate candidate = Candidate.builder()
                .firstname(candidateRequest.getFirstname())
                .lastname(candidateRequest.getLastname())
                .email(candidateRequest.getEmail())
                .status(CandidateStatus.PENDING)
                .build();
        Candidate candi = candidateRepository.save(candidate);
        saveFileToCandidate(candidateRequest.getResume(), candidateRequest.getFirstname() + candidateRequest.getLastname() + "_resume", candi, "RESUME");
        saveFileToCandidate(candidateRequest.getCoverLetter(), candidateRequest.getFirstname() + candidateRequest.getLastname() + "_coverLetter", candi, "COVERLETTER");
        List<Candidate> existingCandidates = job.getCandidates();

        if (existingCandidates == null) {
            existingCandidates = new ArrayList<>();
        }
        existingCandidates.add(candi);
        job.setCandidates(existingCandidates);
        jobPostingRepository.save(job);
        saveModelResponse(candidateRequest.getResume(),job,candi);
    }

    @Async
    public void saveFileToCandidate(MultipartFile file, String name, Candidate candidate, String typeFile) throws IOException {
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) directory.mkdirs();
        String fileName = name + "_" + UUID.randomUUID().toString().substring(0, 8) + ".pdf";
        String filePath = UPLOAD_DIR + fileName;
        Files.write(Paths.get(filePath), file.getBytes());
        if (candidateRepository.findById(candidate.getId()).isPresent()) {
            if (typeFile.equals("RESUME")) {
                candidate.setResume(filePath);
            } else
                candidate.setCoverLetter(filePath);
        }
        candidateRepository.save(candidate);
    }
    @Async
    public void saveModelResponse(MultipartFile file , JobPosting job,Candidate candidate) throws IOException {
        ModelRequest jobModelRequest = ModelRequest.builder()
                .title(job.getTitle())
                .description(job.getDescription())
                .minimumYearsOfExperience(job.getMinimumYearsOfExperience())
                .location(job.getLocation())
                .jobType(job.getJobType().toString())
                .skillsRequired(job.getSkillsRequired())
                .build();
        Map<String,Object> jobModelResponse = JsonUtil.parseJsonString(fastApiClient.sendPdfToGemini(file, jobModelRequest));
        System.out.println(jobModelResponse);
        candidate.setScore((String)jobModelResponse.get("fitScore"));
        int score = Integer.parseInt(((String)jobModelResponse.get("fitScore")).split("/")[0].trim());
        if(score < 5)
            candidate.setStatus(CandidateStatus.REJECTED);
        else {
            candidate.setStrengths((List<String>) jobModelResponse.get("strengths"));
            candidate.setWeaknesses((List<String>) jobModelResponse.get("weaknesses"));
            candidate.setRecommendation((String)jobModelResponse.get("recommendation"));
            candidate.setStatus(CandidateStatus.ACCEPTED);
        }
        System.out.println(candidate);
        candidateRepository.save(candidate);
    }

    public List<Candidate> findAll() {
        return candidateRepository.findAll();
    }

    public Candidate findCandidate(String id) {
        return candidateRepository.findById(id).orElseThrow(() -> new CandidateNotFoundException("This id of candidate not found"));
    }

    public Candidate reject(String id) {
        Candidate candidate = findCandidate(id);
        if (candidate.getStatus() == CandidateStatus.INTERVIEW_SCHEDULED) {
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
