package com.example.demo.dto.book;

import com.example.demo.model.author.Author;

import java.time.LocalDateTime;
import java.util.List;

public record BookDTO(Long id, String title, String isbn, int publicationYear, int availableCopies, int totalCopies, List<Author> authors, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
