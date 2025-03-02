package com.maghrebia.hr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

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
    private int numberOfOpenings;
}
