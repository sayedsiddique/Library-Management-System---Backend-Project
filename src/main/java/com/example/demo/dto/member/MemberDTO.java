package com.example.demo.dto.member;

import com.example.demo.model.member.Status;

import java.time.LocalDateTime;

public record MemberDTO(Long id, String name, String email, String phone, LocalDateTime membershipDate, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
