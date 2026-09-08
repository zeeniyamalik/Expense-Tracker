package com.zeeniya.expense_tracker.repository;

import com.zeeniya.expense_tracker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

}

