package com.maghrebia.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "passwordToken")
public class PasswordToken {
    @Id
    private String id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    @DBRef
    private User user;
}
