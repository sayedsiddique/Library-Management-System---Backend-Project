package com.example.demo.validation;

import com.example.demo.request.borrowTransaction.BorrowTransactionRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DueDateAfterBorrowDateValidator implements ConstraintValidator<DueDateAfterBorrowDate, BorrowTransactionRequest> {
    @Override
    public boolean isValid(BorrowTransactionRequest value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDate borrowDate = value.borrowDate();
        LocalDate dueDate = value.dueDate();

        if (dueDate == null) {
            return true;
        }

        return borrowDate != null && dueDate.isAfter(borrowDate);
    }

    @Override
    public void initialize(DueDateAfterBorrowDate constraintAnnotation) {
    }
}
