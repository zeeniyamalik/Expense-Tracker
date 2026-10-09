package com.zeeniya.expense_tracker.service;

import com.zeeniya.expense_tracker.dto.BudgetRequest;
import com.zeeniya.expense_tracker.entity.Budget;
import com.zeeniya.expense_tracker.entity.Role;
import com.zeeniya.expense_tracker.entity.User;
import com.zeeniya.expense_tracker.exception.ResourceNotFoundException;
import com.zeeniya.expense_tracker.repository.BudgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {
    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private BudgetService budgetService;

    private User currentUser;

    @BeforeEach
    void setUp() {
        currentUser = new User("user@example.com", "user", "Test User", "password", Role.USER);
        currentUser.setId(1L);
    }

    //set
    @Test
    void setBudget_savesBudget_whenNoneAlreadyExists(){
        BudgetRequest request = new BudgetRequest(YearMonth.now(), new BigDecimal("5000.00"));

        when(budgetRepository.findByMonthAndUserId(request.getMonth(), currentUser.getId())).thenReturn(Optional.empty());
        when(budgetRepository.save(any(Budget.class))).thenAnswer(inv -> inv.getArgument(0));

        Budget result = budgetService.setBudget(request, currentUser);

        assertThat(result.getAmount()).isEqualByComparingTo("5000.00");
        assertThat(result.getUser()).isSameAs(currentUser);
        verify(budgetRepository, times(1)).save(any(Budget.class));
    }


    @Test
    void setBudget_throwsException_whenItAlreadyExists() {
        BudgetRequest request = new BudgetRequest(YearMonth.now(), new BigDecimal("5000.00"));
        Budget existingBudget = new Budget(request.getMonth(), request.getAmount());
        existingBudget.setUser(currentUser);

        when(budgetRepository.findByMonthAndUserId(request.getMonth(), currentUser.getId())).thenReturn(Optional.of(existingBudget));

        assertThatThrownBy(() -> budgetService.setBudget(request, currentUser))
                .isInstanceOf(IllegalStateException.class);

        verify(budgetRepository, never()).save(any(Budget.class));
    }

    //get
    @Test
    void getBudget_returnsBudget_whenBudgetFound(){
        BudgetRequest request = new BudgetRequest(YearMonth.now(), new BigDecimal("5000.00"));
        Budget existingBudget = new Budget(request.getMonth(), request.getAmount());
        existingBudget.setUser(currentUser);

        when(budgetRepository.findByMonthAndUserId(request.getMonth(), currentUser.getId())).thenReturn(Optional.of(existingBudget));

        Budget result = budgetService.getBudget(request.getMonth(), currentUser);

        assertThat(result.getAmount()).isEqualByComparingTo("5000.00");
        assertThat(result.getUser()).isSameAs(currentUser);
    }

    @Test
    void getBudget_throwsException_whenNotFound(){
        BudgetRequest request = new BudgetRequest(YearMonth.now(), new BigDecimal("5000.00"));

        when(budgetRepository.findByMonthAndUserId(request.getMonth(), currentUser.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetService.getBudget(request.getMonth(), currentUser)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAllBudget_returnsAllBudgets(){
        BudgetRequest request = new BudgetRequest(YearMonth.now(), new BigDecimal("5000.00"));
        Budget existingBudget = new Budget(request.getMonth(), request.getAmount());
        existingBudget.setUser(currentUser);

        when(budgetRepository.findByUserId(currentUser.getId())).thenReturn(List.of(existingBudget));

        List<Budget> result = budgetService.getAllBudgets(currentUser);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAmount()).isEqualByComparingTo("5000.00");
        assertThat(result.get(0).getUser()).isSameAs(currentUser);

        verify(budgetRepository, times(1)).findByUserId(currentUser.getId());
    }

    //update
    @Test
    void updateBudget_updatesAmount_whenBudgetFound(){
        BudgetRequest request = new BudgetRequest(YearMonth.now(), new BigDecimal("6000.00"));
        Budget existingBudget = new Budget(request.getMonth(), new BigDecimal("5000.00"));
        existingBudget.setUser(currentUser);

        when(budgetRepository.findByMonthAndUserId(request.getMonth(), currentUser.getId())).thenReturn(Optional.of(existingBudget));
        when(budgetRepository.save(any(Budget.class))).thenAnswer(inv -> inv.getArgument(0));

        Budget result = budgetService.updateBudget(request.getMonth(), request, currentUser);

        assertThat(result.getAmount()).isEqualByComparingTo("6000.00");
        assertThat(result.getUser()).isSameAs(currentUser);

        verify(budgetRepository, times(1)).save(any(Budget.class));
    }

    @Test
    void updateBudget_throwsException_whenBudgetNotFound(){
        BudgetRequest request = new BudgetRequest(YearMonth.now(), new BigDecimal("6000.00"));

        when(budgetRepository.findByMonthAndUserId(request.getMonth(), currentUser.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetService.updateBudget(request.getMonth(), request, currentUser)).isInstanceOf(ResourceNotFoundException.class);

        verify(budgetRepository, never()).save(any(Budget.class));
    }

    //delete
    @Test
    void deleteBudget_deletesExistingBudget() {
        YearMonth month = YearMonth.now();

        Budget existingBudget = new Budget(month, new BigDecimal("5000.00"));
        existingBudget.setUser(currentUser);

        when(budgetRepository.findByMonthAndUserId(month, currentUser.getId()))
                .thenReturn(Optional.of(existingBudget));

        budgetService.deleteBudget(month, currentUser);

        verify(budgetRepository, times(1))
                .delete(existingBudget);
    }

    @Test
    void deleteBudget_throwsException_whenBudgetNotFound() {
        YearMonth month = YearMonth.now();

        when(budgetRepository.findByMonthAndUserId(month, currentUser.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetService.deleteBudget(month, currentUser)).isInstanceOf(ResourceNotFoundException.class);

        verify(budgetRepository, never()).delete(any(Budget.class));
    }

    @Test
    void getBudget_throwsException_whenBudgetBelongsToAnotherUser() {
        User otherUser = new User("other@example.com", "other", "Other User", "password", Role.USER);
        otherUser.setId(2L);
        YearMonth month = YearMonth.now();

        when(budgetRepository.findByMonthAndUserId(month, otherUser.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetService.getBudget(month, otherUser))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(budgetRepository, never()).findByMonthAndUserId(month, currentUser.getId());
    }

}
