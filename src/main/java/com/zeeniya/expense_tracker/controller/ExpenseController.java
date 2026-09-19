package com.zeeniya.expense_tracker.controller;

import com.zeeniya.expense_tracker.dto.ExpenseRequest;
import com.zeeniya.expense_tracker.dto.*;
import com.zeeniya.expense_tracker.entity.Expense;
import com.zeeniya.expense_tracker.service.ExpenseService;
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
    public List<Expense> getAllExpenses(){
        return expenseService.getAllExpenses();
    }

    @GetMapping("/{id}")
    public Expense getExpenseById(@PathVariable Long id){
        return expenseService.getExpenseById(id);
    }

    @PostMapping
    public Expense addExpense(@Valid @RequestBody ExpenseRequest request){
        return expenseService.addExpense(request);
    }

    @PutMapping("/{id}")
    public Expense updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseRequest request) {
        return expenseService.updateExpense(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id) {
         expenseService.deleteExpense(id);
    }


    @GetMapping("/summary/category")
    public List<CategorySummary> getCategorySummary() {
            return expenseService.getCategorySummary();
        }

    @GetMapping("/summary/monthly")
    public List<MonthlySummary> getMonthlySummary() {
        return expenseService.getMonthlySummary();
    }

    @GetMapping("/burn-rate")
    public BurnRateWarning getBurnRate() {
        return expenseService.getBurnRate();
    }

    @GetMapping("/forecast")
    public List<ForecastResponse> getForecast() {
        return expenseService.getForecast();
    }

}

