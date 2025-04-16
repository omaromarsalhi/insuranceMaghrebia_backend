package com.maghrebia.user.dto.request;

import com.maghrebia.user.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class EmployeeRegistrationRequest {
    @NotBlank(message = "Firstname should not be empty")
    @Size(min = 2, max = 50, message = "Firstname must be between 2 and 50 characters")
    private String firstname;

    @NotBlank(message = "Lastname should not be empty")
    @Size(min = 2, max = 50, message = "Lastname must be between 2 and 50 characters")
    private String lastname;

    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email is not formatted")
    private String email;

    private List<Role> roles;
}
