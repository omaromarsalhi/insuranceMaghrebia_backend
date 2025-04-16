package com.maghrebia.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "role")
public class Role {
    @Id
    private String id;
    private String name;
    //private List<String> permissions;
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
    public Role(String name) {
        this.name = name;
    }
}
