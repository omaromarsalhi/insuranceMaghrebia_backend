package com.maghrebia.useraction.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Document(collection = "userAction")
public class UserAction {
    @Id
    private String id;
    private String userId;
    private List<Action> actions;
}
