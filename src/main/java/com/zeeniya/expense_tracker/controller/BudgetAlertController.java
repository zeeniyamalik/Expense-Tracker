package com.zeeniya.expense_tracker.controller;

import com.zeeniya.expense_tracker.entity.BudgetAlert;
import com.zeeniya.expense_tracker.service.BudgetAlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class BudgetAlertController {

    private final BudgetAlertService budgetAlertService;

    public BudgetAlertController(BudgetAlertService budgetAlertService) {
        this.budgetAlertService = budgetAlertService;
    }

    @GetMapping("/active")
    public List<BudgetAlert> getActiveAlerts() {
        return budgetAlertService.getActiveAlerts();
    }

    @PutMapping("/{id}/dismiss")
    public void dismissAlert(@PathVariable Long id) {
        budgetAlertService.dismissAlert(id);
    }
}