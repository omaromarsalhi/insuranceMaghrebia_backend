package com.maghrebia.hr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "jobPosting")
public class JobPosting {
    @Id
    private String id;
    private String title;
    private String description;
    @CreatedDate
    private LocalDate postedDate;
    private Boolean isOpen;
    private Integer numberOfOpenings;
    private Integer minimumYearsOfExperience;
    private LocalTime startWorkingHour;
    private LocalTime endWorkingHour;
    private int workingDaysPerWeek;
    private BigDecimal salaryAmount;
    private SalaryType salaryType;
    private Integer numberOfVacations;
    private String location;
    private JobType jobType;
    private Set<String> skillsRequired;
    @DBRef
    private List<Candidate> candidates;
}
