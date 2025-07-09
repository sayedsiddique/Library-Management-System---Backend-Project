package com.example.demo.dto.borrowTransaction;

import com.example.demo.model.borrowTransaction.Status;

import java.time.LocalDate;

public record BorrowTransactionDTO(Long id, Long bookId, String bookTitle, Long memberId, String memberName, LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate, Status status, boolean isOverdue) {
}
