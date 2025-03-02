package com.maghrebia.hr.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidateRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String resume;
    private String coverLetter;
}
