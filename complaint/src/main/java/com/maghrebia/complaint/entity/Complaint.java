package com.maghrebia.complaint.entity;

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
    private String complaintDescription;
    private StatusComplaint complaintStatus;
    private ComplaintType complaintType;
    private LocalDateTime createdAt;

}



