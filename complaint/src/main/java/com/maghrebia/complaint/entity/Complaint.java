package com.maghrebia.complaint.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Document(collection = "complaint")
public class Complaint {
    @Id
    private String complaintId;
    private String userId;
    @NotBlank(message = "Complaint description is required")
    @Size( min=5,max = 500, message = "Complaint description must not exceed 500 characters")
    private String complaintDescription;
    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;
    private StatusComplaint complaintStatus;
    private ComplaintType complaintType;
    private LocalDateTime createdAt;

}



