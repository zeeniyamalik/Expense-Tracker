package com.zeeniya.expense_tracker.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
public class BudgetAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alert_month", unique = true)
    private YearMonth month;

    private String message;
    private boolean dismissed;
    private LocalDateTime createdAt;

    public BudgetAlert() {}

    public BudgetAlert(YearMonth month, String message) {
        this.month = month;
        this.message = message;
        this.dismissed = false;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public YearMonth getMonth() { return month; }
    public String getMessage() { return message; }
    public boolean isDismissed() { return dismissed; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setDismissed(boolean dismissed) { this.dismissed = dismissed; }
}

