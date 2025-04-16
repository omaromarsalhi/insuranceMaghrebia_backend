package com.maghrebia.complaint.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Document(collection = "response")

public class ResponseComplaint {
    @Id
    private String responseId;
    private String complaintId;
    private String responderId;
    @NotBlank(message = "Complaint description is required")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String responseDescription;
    private LocalDateTime createdAt;
    private Boolean isSeen=false;

}
