package com.maghrebia.useraction.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<LocalDate, Integer> dailyScores = new HashMap<>();
}
