package com.zeeniya.expense_tracker.dto;

import com.zeeniya.expense_tracker.entity.Expense;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseResponse {
    private Long id;
    private String category;
    private BigDecimal amount;
    private LocalDate date;
    private String note;

    public static ExpenseResponse from(Expense expense) {
        ExpenseResponse r = new ExpenseResponse();
        r.id = expense.getId();
        r.category = expense.getCategory();
        r.amount = expense.getAmount();
        r.date = expense.getDate();
        r.note = expense.getNote();
        return r;
    }

    public Long getId() { return id; }
    public String getCategory() { return category; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getNote() { return note; }
}