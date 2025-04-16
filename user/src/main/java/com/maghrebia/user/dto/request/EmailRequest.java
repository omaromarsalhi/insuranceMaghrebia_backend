package com.maghrebia.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {

    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email is not formatted")
    private String email;

}
