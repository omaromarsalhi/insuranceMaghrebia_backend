package com.maghrebia.payment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Total amount must be greater than 0")
public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super("Total amount must be greater than 0");
    }

    public InvalidAmountException(String message) {
        super(message);
    }

    public InvalidAmountException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAmountException(Throwable cause) {
        super(cause);
    }
}
