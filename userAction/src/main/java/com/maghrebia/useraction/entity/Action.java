package com.maghrebia.useraction.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Action {
    private String page;
    private String eventType;
    private String action;
    private Long timeSpent;
    private LocalDateTime createdAt;
}
