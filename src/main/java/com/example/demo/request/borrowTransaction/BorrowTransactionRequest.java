package com.example.demo.request.borrowTransaction;

import com.example.demo.validation.DueDateAfterBorrowDate;
import com.example.demo.validation.NotFutureDate;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@DueDateAfterBorrowDate()
public record BorrowTransactionRequest(@NotNull(message = "Book ID is required") Long bookId,
        @NotNull(message = "Member ID is required") Long memberId,
        @NotNull(message = "Borrow date is required")
        @NotFutureDate() LocalDate borrowDate,
        LocalDate dueDate) {
}
