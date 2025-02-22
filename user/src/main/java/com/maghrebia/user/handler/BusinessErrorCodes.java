package com.maghrebia.user.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCodes {
    ACCOUNT_LOCKED("User account is banned", FORBIDDEN),
    ACCOUNT_NOT_ACTIVE("User account is not verified please check your email", FORBIDDEN),
    BAD_CREDENTIALS("Email and/or password is incorrect", BAD_REQUEST),
    INCORRECT_CURRENT_PASSWORD("Incorrect password", BAD_REQUEST),
    NEW_PASSWORD_DOES_NOT_MATCH("New password does not match", BAD_REQUEST),
    ACCESS_DENIED("Access is denied", FORBIDDEN),
    EMAIL_ALREADY_EXISTS("Email already exists", CONFLICT),
    INVALID_TOKEN("Invalid token", BAD_REQUEST),
    EMAIL_PROBLEM("Mail has not been send", FAILED_DEPENDENCY),
    EXPIRED_TOKEN("Activation token has expired . A new token has been sent", BAD_REQUEST),

    ;
    @Getter
    private final String description;

    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(String description, HttpStatus httpStatus) {
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
