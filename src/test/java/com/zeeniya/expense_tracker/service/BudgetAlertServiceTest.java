package com.zeeniya.expense_tracker.service;

import com.zeeniya.expense_tracker.dto.BurnRateWarning;
import com.zeeniya.expense_tracker.entity.BudgetAlert;
import com.zeeniya.expense_tracker.exception.ResourceNotFoundException;
import com.zeeniya.expense_tracker.repository.BudgetAlertRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// TODO: update for user ownership params
@ExtendWith(MockitoExtension.class)
public class BudgetAlertServiceTest {

    @Mock
    private BudgetAlertRepository budgetAlertRepository;

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private BudgetAlertService budgetAlertService;

    @Test
    void checkBurnRate_createsAlert_whenOverBudget() {
        YearMonth currentMonth = YearMonth.now();
        BurnRateWarning burnRate = new BurnRateWarning(
                new BigDecimal("5000.00"),
                new BigDecimal("3000.00"),
                new BigDecimal("200.00"),
                new BigDecimal("6000.00"),
                true,
                true
        );

        when(expenseService.getBurnRate()).thenReturn(burnRate);
        when(budgetAlertRepository.findByMonth(currentMonth)).thenReturn(Optional.empty());

        budgetAlertService.checkBurnRateAndCreateAlert();

        ArgumentCaptor<BudgetAlert> captor = ArgumentCaptor.forClass(BudgetAlert.class);
        verify(budgetAlertRepository).save(captor.capture());
        BudgetAlert savedAlert = captor.getValue();

        assertThat(savedAlert.getMonth()).isEqualTo(currentMonth);
        assertThat(savedAlert.getMessage()).contains("6000.00");
    }

    @Test
    void checkBurnRate_createsNoAlert_whenNoBudget() {
        BurnRateWarning burnRate = new BurnRateWarning(
                new BigDecimal("5000.00"),
                new BigDecimal("3000.00"),
                new BigDecimal("200.00"),
                new BigDecimal("6000.00"),
                false,
                false
        );
        when(expenseService.getBurnRate()).thenReturn(burnRate);
        budgetAlertService.checkBurnRateAndCreateAlert();

        verify(budgetAlertRepository, never()).save(any(BudgetAlert.class));
    }

    @Test
    void checkBurnRate_createsNoAlert_whenNotOverBudget() {
        BurnRateWarning burnRate = new BurnRateWarning(
                new BigDecimal("5000.00"),
                new BigDecimal("3000.00"),
                new BigDecimal("200.00"),
                new BigDecimal("6000.00"),
                false,
                true
        );
        when(expenseService.getBurnRate()).thenReturn(burnRate);
        budgetAlertService.checkBurnRateAndCreateAlert();

        verify(budgetAlertRepository, never()).save(any(BudgetAlert.class));
    }

    @Test
    void checkBurnRate_createsNoAlert_whenBannerAlreadyExists() {
        YearMonth currentMonth = YearMonth.now();
        BurnRateWarning burnRate = new BurnRateWarning(
                new BigDecimal("5000.00"),
                new BigDecimal("3000.00"),
                new BigDecimal("200.00"),
                new BigDecimal("6000.00"),
                true,
                true
        );
        BudgetAlert existingAlert = new BudgetAlert(currentMonth, "Existing alert");

        when(expenseService.getBurnRate()).thenReturn(burnRate);
        when(budgetAlertRepository.findByMonth(currentMonth)).thenReturn(Optional.of(existingAlert));

        budgetAlertService.checkBurnRateAndCreateAlert();

        verify(budgetAlertRepository, never()).save(any(BudgetAlert.class));
    }

    @Test
    void getActiveAlerts_returnsCurrentMonthAlerts(){
        YearMonth currentMonth = YearMonth.now();
        BudgetAlert alert = new BudgetAlert(currentMonth, "Budget Exceeded");

        when(budgetAlertRepository.findByMonthAndDismissedFalse(currentMonth)).thenReturn(List.of(alert));

        List<BudgetAlert> result = budgetAlertService.getActiveAlerts();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMessage()).contains("Budget Exceeded");

        verify(budgetAlertRepository).findByMonthAndDismissedFalse(currentMonth);

    }

    @Test
    void dismissAlert_marksAlertAsDismissed_whenFound(){
        BudgetAlert alert = new BudgetAlert(YearMonth.now(), "Budget exceeded");

        when(budgetAlertRepository.findById(1L)).thenReturn(Optional.of(alert));

        budgetAlertService.dismissAlert(1L);
        assertThat(alert.isDismissed()).isTrue();

        verify(budgetAlertRepository).save(alert);
    }

    @Test
    void dismissAlert_throwsException_whenNotFound(){
        BudgetAlert alert = new BudgetAlert(YearMonth.now(), "Budget exceeded");

        when(budgetAlertRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetAlertService.dismissAlert(99L)).isInstanceOf(ResourceNotFoundException.class);

        verify(budgetAlertRepository, never()).save(any(BudgetAlert.class));
    }



}












