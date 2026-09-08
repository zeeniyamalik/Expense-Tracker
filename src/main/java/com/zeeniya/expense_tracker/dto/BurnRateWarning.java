package com.zeeniya.expense_tracker.dto;

import java.math.BigDecimal;

public class BurnRateWarning {
    private final BigDecimal monthlyBudget;
    private final BigDecimal spentSoFar;
    private final BigDecimal dailyAverage;
    private final BigDecimal projectedTotal;
    private final boolean overBudget;
    private final boolean isBudgetSet;

    public BurnRateWarning(BigDecimal monthlyBudget, BigDecimal spentSoFar, BigDecimal dailyAverage, BigDecimal projectedTotal, boolean overBudget, boolean budgetSet) {
        this.monthlyBudget = monthlyBudget;
        this.spentSoFar = spentSoFar;
        this.dailyAverage = dailyAverage;
        this.projectedTotal = projectedTotal;
        this.overBudget = overBudget;
        this.isBudgetSet = budgetSet;
    }

    public BigDecimal getMonthlyBudget() {
        return monthlyBudget;
    }

    public BigDecimal getSpentSoFar() {
        return spentSoFar;
    }

    public BigDecimal getDailyAverage() {
        return dailyAverage;
    }

    public BigDecimal getProjectedTotal() {
        return projectedTotal;
    }

    public boolean isOverBudget() {
        return overBudget;
    }

    public boolean isBudgetSet() { return isBudgetSet; }
}

