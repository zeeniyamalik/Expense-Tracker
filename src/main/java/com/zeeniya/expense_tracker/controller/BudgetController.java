package com.zeeniya.expense_tracker.controller;

import com.zeeniya.expense_tracker.dto.BudgetRequest;
import com.zeeniya.expense_tracker.entity.Budget;
import com.zeeniya.expense_tracker.entity.User;
import com.zeeniya.expense_tracker.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public Budget setBudget(@Valid @RequestBody BudgetRequest request, @AuthenticationPrincipal User currentUser) {
        return budgetService.setBudget(request, currentUser);
    }

    @GetMapping("/{month}")
    public Budget getBudget(@PathVariable YearMonth month, @AuthenticationPrincipal User currentUser) {
        return budgetService.getBudget(month, currentUser);
    }

    @GetMapping
    public List<Budget> getAllBudgets(@AuthenticationPrincipal User currentUser) {
        return budgetService.getAllBudgets(currentUser);
    }

    @PutMapping("/{month}")
    public Budget updateBudget(@PathVariable YearMonth month, @Valid @RequestBody BudgetRequest request, @AuthenticationPrincipal User currentUser) {
        return budgetService.updateBudget(month, request, currentUser);
    }

    @DeleteMapping("/{month}")
    public void deleteBudget(@PathVariable YearMonth month, @AuthenticationPrincipal User currentUser) {
        budgetService.deleteBudget(month, currentUser);
    }

}

