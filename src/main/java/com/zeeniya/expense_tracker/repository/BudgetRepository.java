package com.zeeniya.expense_tracker.repository;

import com.zeeniya.expense_tracker.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByMonth(YearMonth month); //used in Burn Rate - Expense Service method
}