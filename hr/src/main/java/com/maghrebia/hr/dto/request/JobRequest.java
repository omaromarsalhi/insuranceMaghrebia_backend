package com.maghrebia.hr.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobRequest {
    private String title;
    private String description;
    private int numberOfOpenings;
}
