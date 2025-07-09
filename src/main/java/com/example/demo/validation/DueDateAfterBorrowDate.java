package com.example.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DueDateAfterBorrowDateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DueDateAfterBorrowDate {
    String message() default "Due date must be after borrow date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
