package com.zeeniya.expense_tracker.dto;

import java.math.BigDecimal;

public class ForecastResponse {
    private final String category;
    private final BigDecimal forecast;

    public ForecastResponse(String category, BigDecimal forecast) {
        this.category = category;
        this.forecast = forecast;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getForecast() {
        return forecast;
    }
}
