package com.maghrebia.user.dto.request;

import com.maghrebia.user.entity.Gender;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotBlank(message = "Firstname should not be empty")
    @Size(min = 2, max = 50, message = "Firstname must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Lastname should not be empty")
    @Size(min = 2, max = 50, message = "Lastname must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email is not formatted")
    private String email;

    @NotNull(message = "Date of birth should not be empty")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender should not be empty")
    private Gender gender;

    @NotBlank(message = "Phone number should not be empty")
    @Pattern(regexp = "^[0-9]{8}$", message = "Phone number must be 8 digits")
    private String phone;

    @NotBlank(message = "Address should not be empty")
    @Size(max = 100, message = "Address cannot exceed 100 characters")
    private String address;

    @NotBlank(message = "Password should not be empty")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    private String password;
}