package com.maghrebia.claim.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "responses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Response {
    @Id
    String id;
    @DBRef
    User user;
    String response;
    LocalDateTime respondedAt;
}
