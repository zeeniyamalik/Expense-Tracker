package com.zeeniya.expense_tracker.dto;

import java.math.BigDecimal;

public class CategorySummary {

    private final String category;
    private final BigDecimal total;
    private final double percentage;

    public CategorySummary(String category, BigDecimal amount, double percenatge) {
        this.category = category;
        this.total = amount;
        this.percentage = percenatge;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public double getPercentage() {
        return percentage;
    }
}
