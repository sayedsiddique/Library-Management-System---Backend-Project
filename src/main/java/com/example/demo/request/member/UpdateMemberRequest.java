package com.example.demo.request.member;

import com.example.demo.model.member.Status;

import java.time.LocalDateTime;

public record UpdateMemberRequest(String name,
      String email,
      String phone,
      LocalDateTime membershipDate,
      Status status) {
}
