package com.example.demo.dto.error;

import java.time.ZonedDateTime;

public record ApiError(String message, String status, int statusCode, ZonedDateTime timestamp) {
    public ApiError(String message, String status, int statusCode) {
        this(message, status, statusCode, ZonedDateTime.now());
    }
}
