package com.zeeniya.expense_tracker.ai;

import com.zeeniya.expense_tracker.dto.ExpenseResponse;
import com.zeeniya.expense_tracker.dto.ai.ExpenseParseRequest;
import com.zeeniya.expense_tracker.dto.ai.ExpenseParseResponse;
import com.zeeniya.expense_tracker.dto.ai.InsightResponse;
import com.zeeniya.expense_tracker.entity.User;

import com.zeeniya.expense_tracker.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService, ExpenseService expenseService) {
        this.geminiService = geminiService;
    }

    @GetMapping("/test")
    public ExpenseParseResponse testParse(@RequestParam String input) {
        return geminiService.parseExpense(input);
    }

    @PostMapping("/api/expenses/parse")
    public ExpenseResponse parseAndSave(
            @RequestBody ExpenseParseRequest request,
            @AuthenticationPrincipal User currentUser) {

        return ExpenseResponse.from(geminiService.parseAndSaveExpense(request.getUserInput(), currentUser));
    }

    @GetMapping("/api/ai/insights")
    public InsightResponse getInsights(@AuthenticationPrincipal User currentUser) {
        return geminiService.getSpendingInsights(currentUser);
    }

    @ExceptionHandler(AiUnavailableException.class)
    public ResponseEntity<String> handleAiUnavailable(AiUnavailableException e) {
        return ResponseEntity.status(503).body(e.getMessage());
    }
}

