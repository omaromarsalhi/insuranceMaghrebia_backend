package com.maghrebia.claim.entity;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "claims")
@AllArgsConstructor
public class Claim {
    @Id
    String id;
    @DBRef
    IncidentType type;
    @DBRef
    User user;

    Date submitDate;

}
