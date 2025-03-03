package com.maghrebia.useraction.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Document(collection = "reportResponse")
public class ReportResponse {
    @Id
    private String id;
    @JsonProperty("userAnalysis")
    private String userAnalysis;
    @JsonProperty("classification")
    private String classification;
    @JsonProperty("actions")
    private Map<String, List<ActionStrategy>> actions;
    private LocalDateTime createdAt;
    private String userId;
}
