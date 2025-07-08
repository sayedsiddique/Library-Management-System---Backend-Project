package com.example.demo.request.book;

import jakarta.validation.constraints.*;

public record StoreBookRequest(@NotEmpty(message = "Title must not be empty") String title,
       @NotEmpty(message = "ISBN must not be empty")
       @Pattern(regexp = "^(97[89])?\\d{9}(\\d|X)$", message = "Invalid ISBN format") String isbn,
       @NotNull
       @Min(value = 1000, message = "Publication year must be a valid year") Integer publicationYear,
       @NotNull
       @PositiveOrZero(message = "Available copies must be zero or a positive number") Integer availableCopies,
       @NotNull
       @PositiveOrZero(message = "Total copies must be zero or a positive number") Integer totalCopies) {
}
