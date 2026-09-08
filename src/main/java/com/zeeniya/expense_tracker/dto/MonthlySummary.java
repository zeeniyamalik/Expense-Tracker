package com.zeeniya.expense_tracker.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

public class MonthlySummary {
    private final YearMonth month;
    private final BigDecimal total;

    public MonthlySummary(YearMonth month, BigDecimal total) {
        this.month = month;
        this.total = total;
    }

    public YearMonth getMonth() {
        return month;
    }

    public BigDecimal getTotal() {
        return total;
    }
}



