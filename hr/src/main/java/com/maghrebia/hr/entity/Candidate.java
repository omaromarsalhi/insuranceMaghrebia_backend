package com.maghrebia.hr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "candidate")
public class Candidate {
    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String resume;
    private String coverLetter;
    @CreatedDate
    private LocalDateTime applicationDate;
    private CandidateStatus status;
    private String score;
    private List<String> strengths;
    private List<String> weaknesses;
    private String recommendation;
}
