package com.zeeniya.expense_tracker.service;
//./mvnw -Dtest=BudgetServiceTest#setBudget_savesBudget_whenNoneAlreadyExists test
//./mvnw test

import com.zeeniya.expense_tracker.dto.BudgetRequest;
import com.zeeniya.expense_tracker.entity.Budget;
import com.zeeniya.expense_tracker.exception.ResourceNotFoundException;
import com.zeeniya.expense_tracker.repository.BudgetRepository;
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

    //set
    @Test
    void setBudget_savesBudget_whenNoneAlreadyExists(){
        BudgetRequest request = new BudgetRequest(YearMonth.now(), new BigDecimal("5000.00"));
        Budget existingBudget = new Budget(request.getMonth(), request.getAmount());

        when(budgetRepository.findByMonth(existingBudget.getMonth())).thenReturn(Optional.empty());
        when(budgetRepository.save(any(Budget.class))).thenAnswer(inv -> inv.getArgument(0));

        Budget result = budgetService.setBudget(request);

        assertThat(result.getAmount()).isEqualByComparingTo("5000.00");
        verify(budgetRepository, times(1)).save(any(Budget.class));
    }


    @Test
    void setBudget_throwsException_whenItAlreadyExists() {
        BudgetRequest request = new BudgetRequest(YearMonth.now(), new BigDecimal("5000.00"));
        Budget existingBudget = new Budget(request.getMonth(), request.getAmount());

        when(budgetRepository.findByMonth(request.getMonth())).thenReturn(Optional.of(existingBudget));

        assertThatThrownBy(() -> budgetService.setBudget(request))
                .isInstanceOf(IllegalStateException.class);

        verify(budgetRepository, never()).save(any(Budget.class));
    }

    //get
    @Test
    void getBudget_returnsBudget_whenBudgetFound(){
        BudgetRequest request = new BudgetRequest(YearMonth.now(), new BigDecimal("5000.00"));
        Budget existingBudget = new Budget(request.getMonth(), request.getAmount());

        when(budgetRepository.findByMonth(request.getMonth())).thenReturn(Optional.of(existingBudget));

        Budget result = budgetService.getBudget(request.getMonth());

        assertThat(result.getAmount()).isEqualTo("5000.00");
    }

    @Test
    void getBudget_throwsException_whenNotFound(){
        BudgetRequest request = new BudgetRequest(YearMonth.now(), new BigDecimal("5000.00"));

        when(budgetRepository.findByMonth(request.getMonth())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetService.getBudget(request.getMonth())).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAllBudget_returnsAllBudgets(){
        BudgetRequest request = new BudgetRequest(YearMonth.now(), new BigDecimal("5000.00"));
        Budget existingBudget = new Budget(request.getMonth(), request.getAmount());

        when(budgetRepository.findAll()).thenReturn(List.of(existingBudget));

        List<Budget> result = budgetService.getAllBudgets();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAmount()).isEqualByComparingTo("5000.00");

        verify(budgetRepository, times(1)).findAll();
    }

    //update
    @Test
    void updateBudget_updatesAmount_ifBudgetFound(){
        BudgetRequest request = new BudgetRequest(YearMonth.now(), new BigDecimal("6000.00"));
        Budget existingBudget = new Budget(request.getMonth(), new BigDecimal("5000.00"));

        when(budgetRepository.findByMonth(YearMonth.now())).thenReturn(Optional.of(existingBudget));
        when(budgetRepository.save(any(Budget.class))).thenAnswer(inv -> inv.getArgument(0));

        Budget result = budgetService.updateBudget(YearMonth.now(), request);

        assertThat(result.getAmount()).isEqualByComparingTo("6000.00");

        verify(budgetRepository, times(1)).save(any(Budget.class));
    }

    @Test
    void updateBudget_throwsException_ifBudgetNotFound(){
        BudgetRequest request = new BudgetRequest(YearMonth.now(), new BigDecimal("6000.00"));
        Budget existingBudget = new Budget(request.getMonth(), new BigDecimal("5000.00"));

        when(budgetRepository.findByMonth(request.getMonth())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetService.updateBudget(request.getMonth(), request)).isInstanceOf(ResourceNotFoundException.class);

        verify(budgetRepository, never()).save(any(Budget.class));
    }

    //delete
    @Test
    void deleteBudget_deletesExistingBudget() {
        YearMonth month = YearMonth.now();

        Budget existingBudget = new Budget(month, new BigDecimal("5000.00")
        );

        when(budgetRepository.findByMonth(month))
                .thenReturn(Optional.of(existingBudget));

        budgetService.deleteBudget(month);

        verify(budgetRepository, times(1))
                .delete(existingBudget);
    }

    @Test
    void deleteBudget_throwsException_whenBudgetNotFound() {
        YearMonth month = YearMonth.now();

        when(budgetRepository.findByMonth(month)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetService.deleteBudget(month)).isInstanceOf(ResourceNotFoundException.class);

        verify(budgetRepository, never()).delete(any(Budget.class));
    }

}
