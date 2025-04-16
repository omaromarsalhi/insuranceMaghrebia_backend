package com.maghrebia.claim.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "claims")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Claim {
    @Id
    String id;
    @DBRef
    IncidentType incidentType;
    @DBRef
    User user;
    @DBRef
    List<Response> responses = new ArrayList<>();
    String title;
    String incidentLocation;
    String locationCoordinates;
    LocalDateTime incidentDate;
    LocalDateTime submitDate;
    String description;
    ClaimStatus status;
    List<String> images;

}
