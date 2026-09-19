package com.zeeniya.expense_tracker.controller;

import com.zeeniya.expense_tracker.dto.BudgetRequest;
import com.zeeniya.expense_tracker.entity.Budget;
import com.zeeniya.expense_tracker.service.BudgetService;
import jakarta.validation.Valid;
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
    public Budget setBudget(@Valid @RequestBody BudgetRequest request) {
        return budgetService.setBudget(request);
    }

    @GetMapping("/{month}")
    public Budget getBudget(@PathVariable YearMonth month) {
        return budgetService.getBudget(month);
    }

    @GetMapping
    public List<Budget> getAllBudgets() {
        return budgetService.getAllBudgets();
    }

    @PutMapping("/{month}")
    public Budget updateBudget(@PathVariable YearMonth month, @Valid @RequestBody BudgetRequest request) {
        return budgetService.updateBudget(month, request);
    }

    @DeleteMapping("/{month}")
    public void deleteBudget(@PathVariable YearMonth month) {
        budgetService.deleteBudget(month);
    }

}

