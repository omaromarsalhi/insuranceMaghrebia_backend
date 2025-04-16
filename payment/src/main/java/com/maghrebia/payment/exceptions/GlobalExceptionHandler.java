package com.maghrebia.payment.exceptions;

import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentAlreadyCompletedException.class)
    public ResponseEntity<Map<String, String>> handlePaymentAlreadyCompletedException(PaymentAlreadyCompletedException ex) {
        return new ResponseEntity<>(Collections.singletonMap("message", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<Map<String, String>> handleInvalidAmountException(InvalidAmountException ex) {
        return new ResponseEntity<>(Collections.singletonMap("message", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    public ResponseEntity<Map<String, String>> handleGenericExceptions(Exception ex) {
        return new ResponseEntity<>(Collections.singletonMap("message", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        return new ResponseEntity<>(Collections.singletonMap("message", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StripeException.class)
    public ResponseEntity<Map<String, String>> handleStripeException(StripeException e) {
        return new ResponseEntity<>(Collections.singletonMap("message", "Stripe error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        return new ResponseEntity<>(Collections.singletonMap("message", "An unexpected error occurred: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(PaymentContractNotFoundException.class)
    public ResponseEntity<String> handlePaymentContractNotFoundException(PaymentContractNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}