package com.example.demo.dto.error;

import java.time.ZonedDateTime;
import java.util.Map;

public record ApiError(String message, String status, int statusCode, ZonedDateTime timestamp, Map<String, String> errors) {
    public ApiError(String message, String status, int statusCode) {
        this(message, status, statusCode, ZonedDateTime.now(), Map.of());
    }

    public ApiError(String message, String status, int statusCode, Map<String, String> errors) {
        this(message, status, statusCode, ZonedDateTime.now(), errors);
    }
}
