package com.maghrebia.useraction.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class EmailRequest {
    private String recipient;
    private String subject;
    private String message;
}
