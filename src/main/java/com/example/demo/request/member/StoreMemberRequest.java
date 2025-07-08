package com.example.demo.request.member;

import com.example.demo.model.member.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record StoreMemberRequest(@NotEmpty(message = "Name must not be empty") String name,
     @NotEmpty(message = "Email must not be empty")
     @Email(message = "Email should be valid") String email,
     String phone,
     @NotNull(message = "Status cannot be null") Status status) {
}
