package com.maghrebia.useraction.exception;

public class EmailSendFailedException  extends RuntimeException {
    public EmailSendFailedException(String message) {
        super(message);
    }
}
