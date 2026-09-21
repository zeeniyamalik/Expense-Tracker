package com.zeeniya.expense_tracker.service;

import com.zeeniya.expense_tracker.dto.BurnRateWarning;
import com.zeeniya.expense_tracker.entity.BudgetAlert;
import com.zeeniya.expense_tracker.entity.User;
import com.zeeniya.expense_tracker.exception.ResourceNotFoundException;
import com.zeeniya.expense_tracker.repository.BudgetAlertRepository;

import com.zeeniya.expense_tracker.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
public class BudgetAlertService {

    private final BudgetAlertRepository budgetAlertRepository;
    private final ExpenseService expenseService;
    private final UserRepository userRepository;

    public BudgetAlertService(BudgetAlertRepository budgetAlertRepository, ExpenseService expenseService, UserRepository userRepository) {
        this.budgetAlertRepository = budgetAlertRepository;
        this.expenseService = expenseService;
        this.userRepository = userRepository;
    }

    //runs once a day at 8am, and shows the message once a month for each user
    @Scheduled(cron = "0 0 8 * * *")          //scheduled method calling normal method
    public void checkBurnRateAndCreateAlertsForAllUsers() {
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            checkBurnRateAndCreateAlert(user);
        }
    }

    public void checkBurnRateAndCreateAlert(User currentUser) {
        BurnRateWarning burnRate = expenseService.getBurnRate(currentUser);
        YearMonth currentMonth = YearMonth.now();

        if (!burnRate.isBudgetSet() || !burnRate.isOverBudget()) {
            return;
        }

        boolean alertAlreadyExists = budgetAlertRepository.findByMonthAndUserId(currentMonth, currentUser.getId()).isPresent();
        if (alertAlreadyExists) {
            return;
        }

        String message = "You're projected to spend Rs." + burnRate.getProjectedTotal()
                + " this month, over your budget of Rs." + burnRate.getMonthlyBudget() + "!";

        BudgetAlert alert = new BudgetAlert(currentMonth, message);
        alert.setUser(currentUser);
        budgetAlertRepository.save(alert);
    }

    //alert banner goes away when dismissed
    public List<BudgetAlert> getActiveAlerts(User currentUser) {
        YearMonth currentMonth = YearMonth.now();
        return budgetAlertRepository.findByMonthAndDismissedFalseAndUserId(currentMonth, currentUser.getId());
    }

    public void dismissAlert(Long id, User currentUser) {
        BudgetAlert alert = budgetAlertRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with ID " + id));
        alert.setDismissed(true);
        budgetAlertRepository.save(alert);
    }

}