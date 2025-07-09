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

        // If dueDate is after borrowDate, the validation passes
        return borrowDate != null && dueDate != null && dueDate.isAfter(borrowDate);
    }

    @Override
    public void initialize(DueDateAfterBorrowDate constraintAnnotation) {
    }
}
