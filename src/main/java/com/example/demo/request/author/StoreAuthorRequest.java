package com.example.demo.request.author;

import com.example.demo.validation.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record StoreAuthorRequest(@NotEmpty(message = "Name must not be empty") String name,
     @NotEmpty(message = "Email must not be empty")
     @Email(message = "Email should be valid")
     @UniqueEmail() String email,
     String bio,
     @Past(message = "Birth date must be in the past") LocalDate birthDate) {
}
