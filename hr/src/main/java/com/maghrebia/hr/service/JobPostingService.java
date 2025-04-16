package com.maghrebia.hr.service;

import com.maghrebia.hr.dto.request.JobRequest;
import com.maghrebia.hr.entity.JobPosting;
import com.maghrebia.hr.exception.JobNotFoundException;
import com.maghrebia.hr.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostingService {
    private final JobPostingRepository jobPostingRepository;

    public JobPosting createJob(JobRequest jobRequest) {
        JobPosting job = JobPosting.builder()
                .title(jobRequest.getTitle())
                .description(jobRequest.getDescription())
                .numberOfOpenings(jobRequest.getNumberOfOpenings())
                .minimumYearsOfExperience(jobRequest.getMinimumYearsOfExperience())
                .startWorkingHour(jobRequest.getStartWorkingHour())
                .endWorkingHour(jobRequest.getEndWorkingHour())
                .workingDaysPerWeek(jobRequest.getWorkingDaysPerWeek())
                .salaryAmount(jobRequest.getSalaryAmount())
                .salaryType(jobRequest.getSalaryType())
                .numberOfVacations(jobRequest.getNumberOfVacations())
                .location(jobRequest.getLocation())
                .jobType(jobRequest.getJobType())
                .skillsRequired(jobRequest.getSkillsRequired())
                .isOpen(true)
                .build();
        return jobPostingRepository.save(job);
    }


    public List<JobPosting> findAllJobs() {
        return jobPostingRepository.findAllByOrderByIsOpenDesc();
    }

    public JobPosting findJobById(String id) {
        return jobPostingRepository.findById(id).orElseThrow(() -> new JobNotFoundException("The id of this posting was not found"));
    }

    public List<JobPosting> closeJob(String id) {
        JobPosting jobPosting = findJobById(id);
        jobPosting.setIsOpen(false);
        jobPostingRepository.save(jobPosting);
        return jobPostingRepository.findAllByOrderByIsOpenDesc();
    }

    public List<JobPosting> findAllJobsAvailable() {
        return jobPostingRepository.findAllByIsOpen(true);
    }
}
