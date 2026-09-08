package com.zeeniya.expense_tracker.service;

import com.zeeniya.expense_tracker.dto.BudgetRequest;
import com.zeeniya.expense_tracker.entity.Budget;
import com.zeeniya.expense_tracker.exception.ResourceNotFoundException;
import com.zeeniya.expense_tracker.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public Budget setBudget(BudgetRequest request) {

        if (budgetRepository.findByMonth(request.getMonth()).isPresent()) {
            throw new IllegalStateException(
                    "Budget already exists for " + request.getMonth()
            );
        }

        Budget budget = new Budget(request.getMonth(), request.getAmount());
        return budgetRepository.save(budget);
    }

    public Budget getBudget(YearMonth month) {
        return budgetRepository.findByMonth(month)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found for " + month));
    }

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Budget updateBudget(YearMonth month, BudgetRequest request) {

        Budget existingBudget = getBudget(month);
        existingBudget.setAmount(request.getAmount());

        return budgetRepository.save(existingBudget);
    }

    public void deleteBudget(YearMonth month) {

        Budget existingBudget = getBudget(month);
        budgetRepository.delete(existingBudget);
    }
}

