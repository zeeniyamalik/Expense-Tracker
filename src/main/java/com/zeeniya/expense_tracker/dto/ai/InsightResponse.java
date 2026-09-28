package com.zeeniya.expense_tracker.dto.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InsightResponse {
    private String summary;
    private List<AvoidItem> avoid;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<AvoidItem> getAvoid() {
        return avoid;
    }

    public void setAvoid(List<AvoidItem> avoid) {
        this.avoid = avoid;
    }
}

