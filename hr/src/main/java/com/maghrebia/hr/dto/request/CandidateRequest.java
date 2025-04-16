package com.maghrebia.hr.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidateRequest {
    private String firstname;
    private String lastname;
    private String email;
    private MultipartFile resume;
    private MultipartFile coverLetter;
}
