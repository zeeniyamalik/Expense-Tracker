package com.zeeniya.expense_tracker.ai;

import com.zeeniya.expense_tracker.dto.ai.ExpenseParseRequest;
import com.zeeniya.expense_tracker.dto.ai.ExpenseParseResponse;
import com.zeeniya.expense_tracker.dto.ai.InsightResponse;
import com.zeeniya.expense_tracker.entity.Expense;
import com.zeeniya.expense_tracker.entity.User;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class GeminiController {

    private final GeminiService geminiService;
    //private final ExpenseParsingService expenseParsingService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @GetMapping("/test")
    public ExpenseParseResponse testParse(@RequestParam String input) {
        return geminiService.parseExpense(input);
    }

    @PostMapping("/api/expenses/parse")
    public Expense parseAndSave(
            @RequestBody ExpenseParseRequest request,
            @AuthenticationPrincipal User currentUser) {

        return geminiService.parseAndSaveExpense(request.getUserInput(), currentUser);
    }

    @GetMapping("/api/ai/insights")
    public InsightResponse getInsights(@AuthenticationPrincipal User currentUser) {
        return geminiService.getSpendingInsights(currentUser);
    }
}

