package com.example.demo.dto.book;

import java.time.LocalDateTime;

public record BookDTO(Long id, String title, String isbn, int publicationYear, int availableCopies, int totalCopies, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
