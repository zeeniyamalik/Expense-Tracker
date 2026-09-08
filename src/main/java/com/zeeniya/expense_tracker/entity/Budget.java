package com.zeeniya.expense_tracker.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.YearMonth;

@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "budget_month", unique = true)
    private YearMonth month;

    private BigDecimal amount;

    public Budget() {}

    public Budget(YearMonth month, BigDecimal amount) {
        this.month = month;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public YearMonth getMonth() {
        return month;
    }

    public void setMonth(YearMonth month) {
        this.month = month;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}