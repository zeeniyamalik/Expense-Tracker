package com.zeeniya.expense_tracker.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
public class BudgetAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "alert_month", unique = true)
    private YearMonth month;

    private String message;
    private boolean dismissed;
    private LocalDateTime createdAt;

    @ManyToOne
    @Column(name = "user_id", nullable = false)
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDismissed(boolean dismissed) { this.dismissed = dismissed; }
}

