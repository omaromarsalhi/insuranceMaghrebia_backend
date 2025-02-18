package com.maghrebia.payment.exceptions;

public class PaymentAlreadyCompletedException extends RuntimeException {


    public PaymentAlreadyCompletedException(String message) {
        super(message);
    }


    public PaymentAlreadyCompletedException(String message, Throwable cause) {
        super(message, cause);
    }


    public PaymentAlreadyCompletedException(Throwable cause) {
        super(cause);
    }
}
