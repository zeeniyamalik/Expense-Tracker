package com.zeeniya.expense_tracker.service;

import com.zeeniya.expense_tracker.dto.BurnRateWarning;
import com.zeeniya.expense_tracker.entity.BudgetAlert;
import com.zeeniya.expense_tracker.entity.Role;
import com.zeeniya.expense_tracker.entity.User;
import com.zeeniya.expense_tracker.exception.ResourceNotFoundException;
import com.zeeniya.expense_tracker.repository.BudgetAlertRepository;
import com.zeeniya.expense_tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BudgetAlertServiceTest {

    @Mock
    private BudgetAlertRepository budgetAlertRepository;

    @Mock
    private ExpenseService expenseService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BudgetAlertService budgetAlertService;

    private User currentUser;

    @BeforeEach
    void setUp() {
        currentUser = new User("user@example.com", "user", "Test User", "password", Role.USER);
        currentUser.setId(1L);
    }

    private BurnRateWarning burnRate(boolean overBudget, boolean budgetSet) {
        return new BurnRateWarning(
                new BigDecimal("5000.00"),
                new BigDecimal("3000.00"),
                new BigDecimal("200.00"),
                new BigDecimal("6000.00"),
                overBudget,
                budgetSet,
                new BigDecimal("166.66"),
                false,
                null
        );
    }

    @Test
    void checkBurnRate_createsAlert_whenOverBudget() {
        YearMonth currentMonth = YearMonth.now();
        when(expenseService.getBurnRate(currentUser)).thenReturn(burnRate(true, true));
        when(budgetAlertRepository.findByMonthAndUserId(currentMonth, currentUser.getId()))
                .thenReturn(Optional.empty());

        budgetAlertService.checkBurnRateAndCreateAlert(currentUser);

        ArgumentCaptor<BudgetAlert> captor = ArgumentCaptor.forClass(BudgetAlert.class);
        verify(budgetAlertRepository).save(captor.capture());
        BudgetAlert savedAlert = captor.getValue();

        assertThat(savedAlert.getMonth()).isEqualTo(currentMonth);
        assertThat(savedAlert.getMessage()).contains("6000.00");
        assertThat(savedAlert.getUser()).isSameAs(currentUser);
    }

    @Test
    void checkBurnRate_createsNoAlert_whenNoBudget() {
        when(expenseService.getBurnRate(currentUser)).thenReturn(burnRate(true, false));

        budgetAlertService.checkBurnRateAndCreateAlert(currentUser);

        verify(budgetAlertRepository, never()).save(any(BudgetAlert.class));
    }

    @Test
    void checkBurnRate_createsNoAlert_whenNotOverBudget() {
        when(expenseService.getBurnRate(currentUser)).thenReturn(burnRate(false, true));

        budgetAlertService.checkBurnRateAndCreateAlert(currentUser);

        verify(budgetAlertRepository, never()).save(any(BudgetAlert.class));
    }

    @Test
    void checkBurnRate_createsNoAlert_whenBannerAlreadyExists() {
        YearMonth currentMonth = YearMonth.now();
        BudgetAlert existingAlert = new BudgetAlert(currentMonth, "Existing alert");
        existingAlert.setUser(currentUser);

        when(expenseService.getBurnRate(currentUser)).thenReturn(burnRate(true, true));
        when(budgetAlertRepository.findByMonthAndUserId(currentMonth, currentUser.getId()))
                .thenReturn(Optional.of(existingAlert));

        budgetAlertService.checkBurnRateAndCreateAlert(currentUser);

        verify(budgetAlertRepository, never()).save(any(BudgetAlert.class));
    }

    @Test
    void getActiveAlerts_returnsCurrentMonthAlertsForUser() {
        YearMonth currentMonth = YearMonth.now();
        BudgetAlert alert = new BudgetAlert(currentMonth, "Budget Exceeded");
        alert.setUser(currentUser);

        when(budgetAlertRepository.findByMonthAndDismissedFalseAndUserId(currentMonth, currentUser.getId()))
                .thenReturn(List.of(alert));

        List<BudgetAlert> result = budgetAlertService.getActiveAlerts(currentUser);

        assertThat(result).containsExactly(alert);
        verify(budgetAlertRepository).findByMonthAndDismissedFalseAndUserId(currentMonth, currentUser.getId());
    }

    @Test
    void dismissAlert_marksAlertAsDismissed_whenFoundForUser() {
        BudgetAlert alert = new BudgetAlert(YearMonth.now(), "Budget exceeded");
        alert.setUser(currentUser);
        when(budgetAlertRepository.findByIdAndUserId(1L, currentUser.getId()))
                .thenReturn(Optional.of(alert));

        budgetAlertService.dismissAlert(1L, currentUser);

        assertThat(alert.isDismissed()).isTrue();
        verify(budgetAlertRepository).save(alert);
    }

    @Test
    void dismissAlert_throwsException_whenNotFoundForUser() {
        when(budgetAlertRepository.findByIdAndUserId(99L, currentUser.getId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetAlertService.dismissAlert(99L, currentUser))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(budgetAlertRepository, never()).save(any(BudgetAlert.class));
    }

    @Test
    void checkBurnRateForAllUsers_checksEachUser() {
        User otherUser = new User("other@example.com", "other", "Other User", "password", Role.USER);
        otherUser.setId(2L);
        when(userRepository.findAll()).thenReturn(List.of(currentUser, otherUser));
        when(expenseService.getBurnRate(currentUser)).thenReturn(burnRate(false, true));
        when(expenseService.getBurnRate(otherUser)).thenReturn(burnRate(false, true));

        budgetAlertService.checkBurnRateAndCreateAlertsForAllUsers();

        verify(expenseService).getBurnRate(currentUser);
        verify(expenseService).getBurnRate(otherUser);
    }
}
