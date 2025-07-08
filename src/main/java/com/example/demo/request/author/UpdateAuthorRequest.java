package com.example.demo.request.author;

import java.time.LocalDate;

public record UpdateAuthorRequest(String name,
      String email,
      String bio,
      LocalDate birthDate) {
}
