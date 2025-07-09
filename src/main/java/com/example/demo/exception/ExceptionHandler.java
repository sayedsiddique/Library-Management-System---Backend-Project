package com.example.demo.exception;

import com.example.demo.dto.error.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handle(ResourceNotFoundException exception, HttpServletRequest request) {
        ApiError error = new ApiError(exception.getMessage(), "error", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<ApiError>(error, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiError> handle(DuplicateEmailException exception, HttpServletRequest request) {
        ApiError error = new ApiError(exception.getMessage(), "error", HttpStatus.CONFLICT.value());
        return new ResponseEntity<ApiError>(error, HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicateIsbnException.class)
    public ResponseEntity<ApiError> handle(DuplicateIsbnException exception, HttpServletRequest request) {
        ApiError error = new ApiError(exception.getMessage(), "error", HttpStatus.CONFLICT.value());
        return new ResponseEntity<ApiError>(error, HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handle(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> validationErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (msg1, msg2) -> msg1
                ));
        ApiError error = new ApiError("Validation failed", "error", HttpStatus.BAD_REQUEST.value(), validationErrors);
        return new ResponseEntity<ApiError>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<ApiError> handle(BookNotAvailableException exception, HttpServletRequest request) {
        ApiError error = new ApiError(exception.getMessage(), "error", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<ApiError>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BorrowLimitExceededException.class)
    public ResponseEntity<ApiError> handle(BorrowLimitExceededException exception, HttpServletRequest request) {
        ApiError error = new ApiError(exception.getMessage(), "error", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<ApiError>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicateBorrowException.class)
    public ResponseEntity<ApiError> handle(DuplicateBorrowException exception, HttpServletRequest request) {
        ApiError error = new ApiError(exception.getMessage(), "error", HttpStatus.CONFLICT.value());
        return new ResponseEntity<ApiError>(error, HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ApiError> handle(InvalidDateException exception, HttpServletRequest request) {
        ApiError error = new ApiError(exception.getMessage(), "error", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<ApiError>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handle(IllegalStateException exception, HttpServletRequest request) {
        ApiError error = new ApiError(exception.getMessage(), "error", HttpStatus.CONFLICT.value());
        return new ResponseEntity<ApiError>(error, HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handle(Exception exception, HttpServletRequest request) {
        ApiError error = new ApiError(exception.getMessage(), "error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<ApiError>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
