package com.maghrebia.complaint.entity;

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
    //l user qui va repond a  la reclamation
    private String responderId;
    private String responseDescription;
    private LocalDateTime createdAt;
}
