package com.maghrebia.hr.dto.request;

import lombok.*;
import org.springframework.core.io.ByteArrayResource;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelRequest {
    private String title;
    private String description;
    private Integer minimumYearsOfExperience;
    private String location;
    private String jobType;
    private Set<String> skillsRequired;
}
