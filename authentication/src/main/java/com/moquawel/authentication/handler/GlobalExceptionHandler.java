package com.moquawel.authentication.handler;

import com.moquawel.authentication.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException exp) {
        Map<String, String> errors = new HashMap<>();
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }


    @ExceptionHandler(EmailSendFailedException.class)
    public ResponseEntity<?> handleException(EmailSendFailedException exp) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Password Email: ", exp.getMessage());
        return ResponseEntity
                .status(HttpStatus.IM_USED)
                .body(errors);
    }

    @ExceptionHandler(DataMismatchException.class)
    public ResponseEntity<?> handleException(DataMismatchException exp) {
        Map<String, String> errors = new HashMap<>();
        errors.put("user", exp.getMessage());
        return ResponseEntity
                .status(HttpStatus.IM_USED)
                .body(errors);
    }

    @ExceptionHandler(UserNameAlreadyExists.class)
    public ResponseEntity<?> handleException(UserNameAlreadyExists exp) {
        Map<String, String> errors = new HashMap<>();
        errors.put("user", exp.getMessage());
        return ResponseEntity
                .status(HttpStatus.IM_USED)
                .body(errors);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleException(UserNotFoundException exp) {
        Map<String, String> errors = new HashMap<>();
        errors.put("user", exp.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errors);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> handleTokenExpiredException(TokenExpiredException exp) {
        Map<String, String> errors = new HashMap<>();
        errors.put("token", exp.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errors);
    }

    @ExceptionHandler(TokenMissingException.class)
    public ResponseEntity<?> handleTokenExpiredException(TokenMissingException exp) {
        Map<String, String> errors = new HashMap<>();
        errors.put("token", exp.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errors);
    }

//    @ExceptionHandler(ServletException.class)
//    public ResponseEntity<?> handleException(ServletException exp) {
//        Map<String, Boolean> errors = new HashMap<>();
//        errors.put("invalid_token", true);
//        return ResponseEntity
//                .status(HttpStatus.UNAUTHORIZED)
//                .body(errors);
//    }




//
//    private void handlePaymentException(BusinessException exp) {
//
//    }
//
//    @ExceptionHandler({EntityNotFoundException.class})
//    public ResponseEntity<?> handleException(EntityNotFoundException exp) {
//
//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .body(exp.getMessage());
//    }
//
//    @ExceptionHandler({DisabledException.class})
//    public ResponseEntity<?> handleException(DisabledException exp) {
//
//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .body("User account is disabled, please contact your admin.");
//    }
//
//    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
//    public ResponseEntity<?> handleException(MethodArgumentNotValidException exp) {
//        Map<String, String> errors = new HashMap<>();
//        // errorCode (dictionary): fieldName
//        // 1: firstname
//        // 3: email
//        // 4: email
//        exp.getBindingResult().getAllErrors()
//                .forEach(error -> {
//                    String fieldName = ((FieldError) error).getField();
//                    String errorMessage = error.getDefaultMessage();
//                    errors.put(errorMessage, fieldName);
//                });
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(errors);
//
//    }
}
