package com.maghrebia.claim.entity;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "claims")
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
}
