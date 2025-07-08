package com.example.demo.dto.response;

import java.time.ZonedDateTime;

public record ApiResponse<T>(String message, String status, int statusCode, T data, ZonedDateTime timestamp) {
    public ApiResponse(String message, String status, int statusCode, T data) {
        this(message, status, statusCode, data, ZonedDateTime.now());
    }
}
