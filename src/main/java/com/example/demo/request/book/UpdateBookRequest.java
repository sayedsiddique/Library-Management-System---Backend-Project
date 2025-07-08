package com.example.demo.request.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateBookRequest(String title,
        @Pattern(regexp = "^(97[89])?\\d{9}(\\d|X)$", message = "Invalid ISBN format") String isbn,
        @Min(value = 1000, message = "Publication year must be a valid year") Integer publicationYear,
        @PositiveOrZero(message = "Available copies must be zero or a positive number") Integer availableCopies,
        @PositiveOrZero(message = "Total copies must be zero or a positive number") Integer totalCopies) {
}
