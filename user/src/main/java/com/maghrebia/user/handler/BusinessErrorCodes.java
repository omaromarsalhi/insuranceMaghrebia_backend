package com.maghrebia.user.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCodes {
    ACCOUNT_LOCKED("Your account is banned", FORBIDDEN),
    ACCOUNT_NOT_ACTIVE("Your account is not verified please check your email", FORBIDDEN),
    ACCOUNT_NOT_FOUND("This email is not registered", NOT_FOUND),
    BAD_CREDENTIALS("Email and/or password is incorrect", BAD_REQUEST),
    INCORRECT_CURRENT_PASSWORD("Incorrect password", BAD_REQUEST),
    NEW_PASSWORD_DOES_NOT_MATCH("New password does not match", BAD_REQUEST),
    ACCESS_DENIED("Access is denied", FORBIDDEN),
    EMAIL_ALREADY_EXISTS("Email already exists", CONFLICT),
    INVALID_TOKEN("Invalid token", BAD_REQUEST),
    EMAIL_PROBLEM("Mail has not been send", FAILED_DEPENDENCY),
    EXPIRED_TOKEN("Activation token has expired . A new token has been sent", BAD_REQUEST),
    EXPIRED_PASSWORD_TOKEN("This link is expired", FORBIDDEN),
    INVALID_PASSWORD_TOKEN("This link is invalid", FORBIDDEN),
    EMAIL_CHANGE_REQUIRES_VERIFICATION("Email change requires verification", UNPROCESSABLE_ENTITY),
    CURRENT_PASSWORD_DOES_NOT_MATCH("The current password does not match the existing password.", BAD_REQUEST ),;
    @Getter
    private final String description;

    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(String description, HttpStatus httpStatus) {
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
