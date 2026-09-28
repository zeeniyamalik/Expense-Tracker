package com.zeeniya.expense_tracker.dto.ai;

import java.util.List;

public class GeminiResponse {

    private List<GeminiCandidate> candidates;

    public List<GeminiCandidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<GeminiCandidate> candidates) {
        this.candidates = candidates;
    }
}
