package com.zeeniya.expense_tracker.controller;

import com.zeeniya.expense_tracker.dto.ExpenseRequest;
import com.zeeniya.expense_tracker.dto.*;
import com.zeeniya.expense_tracker.entity.Expense;
import com.zeeniya.expense_tracker.entity.User;
import com.zeeniya.expense_tracker.service.ExpenseService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

   private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<Expense> getAllExpenses(@AuthenticationPrincipal User currentUser){
        return expenseService.getAllExpenses(currentUser);
    }

    @GetMapping("/{id}")
    public Expense getExpenseById(@PathVariable Long id,  @AuthenticationPrincipal User currentUser){
        return expenseService.getExpenseById(id, currentUser);
    }

    @PostMapping
    public Expense addExpense(@Valid @RequestBody ExpenseRequest request, @AuthenticationPrincipal User currentUser){
        return expenseService.addExpense(request, currentUser);
    }

    @PutMapping("/{id}")
    public Expense updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseRequest request,  @AuthenticationPrincipal User currentUser) {
        return expenseService.updateExpense(id, request, currentUser);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id,  @AuthenticationPrincipal User currentUser) {
         expenseService.deleteExpense(id, currentUser);
    }


    @GetMapping("/summary/category")
    public List<CategorySummary> getCategorySummary(@AuthenticationPrincipal User currentUser) {
            return expenseService.getCategorySummary(currentUser);
        }

    @GetMapping("/summary/monthly")
    public List<MonthlySummary> getMonthlySummary( @AuthenticationPrincipal User currentUser) {
        return expenseService.getMonthlySummary(currentUser);
    }

    @GetMapping("/burn-rate")
    public BurnRateWarning getBurnRate( @AuthenticationPrincipal User currentUser) {
        return expenseService.getBurnRate(currentUser);
    }

    @GetMapping("/forecast")
    public List<ForecastResponse> getForecast( @AuthenticationPrincipal User currentUser) {
        return expenseService.getForecast(currentUser);
    }

}

