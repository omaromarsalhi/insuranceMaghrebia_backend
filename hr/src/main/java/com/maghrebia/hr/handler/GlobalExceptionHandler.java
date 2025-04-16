package com.maghrebia.hr.handler;

import com.maghrebia.hr.dto.response.ExceptionResponse;
import com.maghrebia.hr.exception.CandidateNotFoundException;
import com.maghrebia.hr.exception.InterviewNotFoundException;
import com.maghrebia.hr.exception.JobNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ExceptionResponse> handleException(String message, Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .message(message)
                        .error(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(JobNotFoundException.class)
    public ResponseEntity<ExceptionResponse> jobNotFound(JobNotFoundException e) {
        return handleException("Job not found", e);
    }

    @ExceptionHandler(CandidateNotFoundException.class)
    public ResponseEntity<ExceptionResponse> candidateNotFound(CandidateNotFoundException e) {
        return handleException("Candidate not found", e);
    }

    @ExceptionHandler(InterviewNotFoundException.class)
    public ResponseEntity<ExceptionResponse> interviewNotFound(InterviewNotFoundException e) {
        return handleException("Interview not found", e);
    }
}
