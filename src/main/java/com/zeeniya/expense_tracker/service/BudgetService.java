package com.zeeniya.expense_tracker.service;

import com.zeeniya.expense_tracker.dto.BudgetRequest;
import com.zeeniya.expense_tracker.entity.Budget;
import com.zeeniya.expense_tracker.entity.User;
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


    public Budget setBudget(BudgetRequest request, User currentUser) {
        if (budgetRepository.findByMonthAndUserId(request.getMonth(), currentUser.getId()).isPresent()) {
            throw new IllegalStateException(
                    "Budget already exists for " + request.getMonth()
            );
        }

        Budget budget = new Budget(request.getMonth(), request.getAmount());
        budget.setUser(currentUser);
        return budgetRepository.save(budget);
    }

    public Budget getBudget(YearMonth month, User currentUser) {
        return budgetRepository.findByMonthAndUserId(month, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found for " + month));
    }

    public List<Budget> getAllBudgets(User currentUser) {
        return budgetRepository.findByUserId(currentUser.getId());
    }

    public Budget updateBudget(YearMonth month, BudgetRequest request, User currentUser) {
        Budget existingBudget = getBudget(month, currentUser);
        existingBudget.setAmount(request.getAmount());

        return budgetRepository.save(existingBudget);
    }

    public void deleteBudget(YearMonth month, User currentUser) {
        Budget existingBudget = getBudget(month, currentUser);

        budgetRepository.delete(existingBudget);
    }
}

