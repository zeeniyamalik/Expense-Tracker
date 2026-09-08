package com.zeeniya.expense_tracker.service;

import com.zeeniya.expense_tracker.dto.BurnRateWarning;
import com.zeeniya.expense_tracker.entity.BudgetAlert;
import com.zeeniya.expense_tracker.exception.ResourceNotFoundException;
import com.zeeniya.expense_tracker.repository.BudgetAlertRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
public class BudgetAlertService {

    private final BudgetAlertRepository budgetAlertRepository;
    private final ExpenseService expenseService;

    public BudgetAlertService(BudgetAlertRepository budgetAlertRepository, ExpenseService expenseService) {
        this.budgetAlertRepository = budgetAlertRepository;
        this.expenseService = expenseService;
    }

    //runs once a day at 8am, and shows the message once a month
    @Scheduled(cron = "0 0 8 * * *")
    public void checkBurnRateAndCreateAlert() {
        BurnRateWarning burnRate = expenseService.getBurnRate();
        YearMonth currentMonth = YearMonth.now();

        if (!burnRate.isBudgetSet() || !burnRate.isOverBudget()) {
            return;
        }

        boolean alertAlreadyExists = budgetAlertRepository.findByMonth(currentMonth).isPresent();
        if (alertAlreadyExists) {
            return;
        }

        String message = "You're projected to spend Rs." + burnRate.getProjectedTotal()
                + " this month, over your budget of Rs." + burnRate.getMonthlyBudget() + "!";

        budgetAlertRepository.save(new BudgetAlert(currentMonth, message));
    }

    //alert banner goes away when dismissed
    public List<BudgetAlert> getActiveAlerts() {
        YearMonth currentMonth = YearMonth.now();
        return budgetAlertRepository.findByMonthAndDismissedFalse(currentMonth);
    }

    public void dismissAlert(Long id) {
        BudgetAlert alert = budgetAlertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with ID " + id));
        alert.setDismissed(true);
        budgetAlertRepository.save(alert);
    }

}