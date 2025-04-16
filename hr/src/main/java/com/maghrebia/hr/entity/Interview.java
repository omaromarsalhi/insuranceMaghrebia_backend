package com.maghrebia.hr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "interview")
public class Interview {
    @Id
    private String id;
    @DBRef
    private Candidate candidateId;
    private LocalDateTime scheduledDate;
    private String location;
    private InterviewStatus status;
}
