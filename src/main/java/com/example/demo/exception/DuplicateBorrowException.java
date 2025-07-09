package com.example.demo.exception;

public class DuplicateBorrowException extends RuntimeException {
    public DuplicateBorrowException(String message) {
        super(message);
    }
}
