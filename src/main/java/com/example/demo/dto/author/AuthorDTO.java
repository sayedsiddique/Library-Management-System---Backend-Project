package com.example.demo.dto.author;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AuthorDTO(Long id, String name, String email, String bio, LocalDate birthDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
