package com.maghrebia.hr.dto.request;

import com.maghrebia.hr.entity.JobType;
import com.maghrebia.hr.entity.SalaryType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobRequest {
    private String title;
    private String description;
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
    @Override
    public String toString() {
        return "JobRequest{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", numberOfOpenings=" + numberOfOpenings +
                ", minimumYearsOfExperience=" + minimumYearsOfExperience +
                ", startWorkingHour=" + startWorkingHour +
                ", endWorkingHour=" + endWorkingHour +
                ", workingDaysPerWeek=" + workingDaysPerWeek +
                ", salaryAmount=" + salaryAmount +
                ", salaryType=" + salaryType +
                ", numberOfVacations=" + numberOfVacations +
                ", location='" + location + '\'' +
                ", jobType=" + jobType +
                ", skillsRequired=" + skillsRequired +
                '}';
    }
}
