package com.zeeniya.expense_tracker.repository;

import com.zeeniya.expense_tracker.entity.BudgetAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.*;

public interface BudgetAlertRepository extends JpaRepository<BudgetAlert, Long> {
    Optional<BudgetAlert> findByMonth(YearMonth month);
    List<BudgetAlert> findByMonthAndDismissedFalse(YearMonth month);
}
