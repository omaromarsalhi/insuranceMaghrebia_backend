package com.maghrebia.claim.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "IncidentTypes")
@AllArgsConstructor
@Getter
@Setter
public class IncidentType {
    @Id
    String id;
    String name;
    String description;
    LocalDateTime created;
    boolean active;
}
