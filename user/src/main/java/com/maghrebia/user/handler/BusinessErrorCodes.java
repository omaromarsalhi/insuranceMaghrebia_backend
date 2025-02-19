package com.maghrebia.user.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCodes {
    ACCOUNT_LOCKED("User account is locked", UNAUTHORIZED),
    ACCOUNT_NOT_ACTIVE("User account is not active", UNAUTHORIZED),
    BAD_CREDENTIALS("Login and/or password is incorrect", UNAUTHORIZED),
    INCORRECT_CURRENT_PASSWORD("Incorrect password", BAD_REQUEST),
    NEW_PASSWORD_DOES_NOT_MATCH("New password does not match", BAD_REQUEST),
    ACCESS_DENIED("Access is denied", FORBIDDEN),
    INTERNAL_SERVER_ERROR("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
    MAIL_NOT_SENT("Mail not sent", HttpStatus.INTERNAL_SERVER_ERROR ),
    EMAIL_ALREADY_EXISTS("Email already exists", CONFLICT);

    @Getter
    private final String description;

    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(String description, HttpStatus httpStatus) {
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
