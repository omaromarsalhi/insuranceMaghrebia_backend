package com.maghrebia.user.handler;

import com.maghrebia.user.dto.response.ExceptionResponse;
import com.maghrebia.user.exception.*;
import com.mongodb.DuplicateKeyException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static com.maghrebia.user.handler.BusinessErrorCodes.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ExceptionResponse> buildResponse(BusinessErrorCodes errorCode, Exception exp) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorDescription(errorCode.getDescription())
                        .error(exp.getMessage())
                        .build());
    }


    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleLockedException(LockedException exp) {
        return buildResponse(ACCOUNT_LOCKED, exp);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException exp) {
        return buildResponse(BAD_CREDENTIALS, exp);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleDisabledException(DisabledException exp) {
        return buildResponse(ACCOUNT_NOT_ACTIVE, exp);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException exp) {
        return buildResponse(ACCESS_DENIED, exp);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException exp) {
        return buildResponse(EMAIL_ALREADY_EXISTS, exp);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        Set<String> errors = new HashSet<>();
        exp.getBindingResult().getAllErrors().forEach(error -> {
            var errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .validationErrors(errors)
                                .build()
                );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidTokenException(InvalidTokenException exp) {
        return buildResponse(INVALID_TOKEN, exp);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleMessagingException(MessagingException exp) {
        return buildResponse(EMAIL_PROBLEM, exp);
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ExceptionResponse> handleExpiredTokenException(ExpiredTokenException exp) {
        return buildResponse(EXPIRED_TOKEN, exp);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEmailNotFoundException(EmailNotFoundException exp) {
        return buildResponse(ACCOUNT_NOT_FOUND,exp);
    }
    @ExceptionHandler(PasswordDoesNotMatchException.class)
    public ResponseEntity<ExceptionResponse> handlePasswordDoesNotMatchException(PasswordDoesNotMatchException exp) {
        return buildResponse(NEW_PASSWORD_DOES_NOT_MATCH,exp);
    }
    @ExceptionHandler(LinkInvalidException.class)
    public ResponseEntity<ExceptionResponse> handleLinkInvalidException(LinkInvalidException exp) {
        return buildResponse(INVALID_PASSWORD_TOKEN,exp);
    }
    @ExceptionHandler(LinkExpiredException.class)
    public ResponseEntity<ExceptionResponse> handleLinkExpiredException(LinkExpiredException exp) {
        return buildResponse(EXPIRED_PASSWORD_TOKEN,exp);
    }
}
