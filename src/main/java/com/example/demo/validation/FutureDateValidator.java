package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class FutureDateValidator implements ConstraintValidator<NotFutureDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || !value.isAfter(LocalDate.now());
    }
}
