package com.maghrebia.hr.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewRequest {
    private LocalDateTime scheduledDate;
    private String location;
}
