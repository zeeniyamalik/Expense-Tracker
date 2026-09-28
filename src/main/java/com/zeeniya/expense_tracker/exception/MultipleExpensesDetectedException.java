package com.zeeniya.expense_tracker.exception;

public class MultipleExpensesDetectedException extends RuntimeException {
    public MultipleExpensesDetectedException(String message) {
        super(message);
    }
}