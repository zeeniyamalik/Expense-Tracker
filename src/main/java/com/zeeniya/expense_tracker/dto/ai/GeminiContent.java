package com.zeeniya.expense_tracker.dto.ai;

import java.util.List;

public class GeminiContent {

    private List<GeminiPart> parts;

    public List<GeminiPart> getParts() {
        return parts;
    }

    public void setParts(List<GeminiPart> parts) {
        this.parts = parts;
    }
}