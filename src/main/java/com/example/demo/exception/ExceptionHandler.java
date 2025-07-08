package com.example.demo.exception;

import com.example.demo.dto.error.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handle(ResourceNotFoundException exception, HttpServletRequest request) {
        ApiError error = new ApiError(exception.getMessage(), "error", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<ApiError>(error, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handle(Exception exception, HttpServletRequest request) {
        ApiError error = new ApiError(exception.getMessage(), "error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<ApiError>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
