//package com.zeeniya.expense_tracker.service;
//// ./mvnw -Dtest=ExpenseServiceTest#getExpenseById_returnsExpense_ifFound test
//
//import com.zeeniya.expense_tracker.dto.*;
////import com.zeeniya.expense_tracker.entity.Budget;
//import com.zeeniya.expense_tracker.entity.Budget;
//import com.zeeniya.expense_tracker.entity.Expense;
//import com.zeeniya.expense_tracker.exception.ResourceNotFoundException;
//
//import com.zeeniya.expense_tracker.repository.BudgetRepository;
//import com.zeeniya.expense_tracker.repository.ExpenseRepository;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//// TODO: update for user ownership params
//@ExtendWith(MockitoExtension.class)
//class ExpenseServiceTest {
//    private Expense sampleExpense;
//    @Mock
//    private ExpenseRepository expenseRepository;
//
//    @Mock
//    private BudgetRepository budgetRepository;
//
//    @InjectMocks
//    private ExpenseService expenseService;
//
//    @BeforeEach
//    void setUp() {
//        sampleExpense = new Expense("Food", new BigDecimal("500.00"), LocalDate.now(), "");
//        sampleExpense.setId(1L);
//    }
//
//    //get
//    @Test
//    void getAllExpenses_returnsAllExpenses(){
//        Expense expense = new Expense("Food", new BigDecimal("500.00"), LocalDate.now(), "");
//
//        when(expenseRepository.findAll()).thenReturn(List.of(expense));
//
//        List<Expense> result = expenseService.getAllExpenses();
//
//        assertThat(result).hasSize(1);
//        assertThat(result.get(0).getCategory()).isEqualTo("Food");
//        assertThat(result.get(0).getAmount()).isEqualByComparingTo("500.00");
//    }
//
//    @Test
//    void getExpenseById_returnsExpense_ifFound(){
//        Expense expense = new Expense("Food", new BigDecimal("500.00"), LocalDate.now(), "");
//
//        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));
//
//        Expense result = expenseService.getExpenseById(1L);
//        assertThat(expense.getCategory()).isEqualTo("Food");
//        assertThat(expense.getAmount()).isEqualByComparingTo("500.00");
//    }
//
//    @Test
//    void getExpenseById_throwsException_ifNotFound(){
//        Expense expense = new Expense("Food", new BigDecimal("500.00"), LocalDate.now(), "");
//
//        when(expenseRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> expenseService.getExpenseById(1L)).isInstanceOf(ResourceNotFoundException.class);
//
//    }
//
//    //add
//    @Test
//    void addExpense_savesExpense_whenDataIsValid(){
//        ExpenseRequest request = new ExpenseRequest("Food", new BigDecimal("500.00"), LocalDate.now(), "");
//        Expense expense = new Expense("Food", new BigDecimal("500.00"), LocalDate.now(), "");
//
//        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
//
//        expenseService.addExpense(request);
//
//        ArgumentCaptor<Expense> captor = ArgumentCaptor.forClass(Expense.class);
//        verify(expenseRepository, times(1)).save(captor.capture());
//        Expense actuallySaved = captor.getValue();
//
//        assertThat(actuallySaved.getCategory()).isEqualTo("Food");
//        assertThat(actuallySaved.getAmount()).isEqualByComparingTo("500.00");
//    }
//
//    @Test
//    void addExpense_throwsException_whenDataIsNotValid(){
//        ExpenseRequest request = new ExpenseRequest("Food", new BigDecimal("500.00"), LocalDate.now().plusDays(1), "");
//
//        assertThatThrownBy(() -> expenseService.addExpense(request)).isInstanceOf(IllegalArgumentException.class);
//
//        verify(expenseRepository, never()).save(any(Expense.class));
//    }
//
//    //update
//    @Test
//    void updateExpense_updatesFields_whenValid() {
//        ExpenseRequest request = new ExpenseRequest("Food", new BigDecimal("500.00"), LocalDate.now(), "");
//
//        when(expenseRepository.findById(1L)).thenReturn(Optional.of(sampleExpense));
//        when(expenseRepository.save(any(Expense.class))).thenAnswer(inv -> inv.getArgument(0));
//
//        Expense result = expenseService.updateExpense(1L, request);
//
//        assertThat(result.getCategory()).isEqualTo("Food");
//        assertThat(result.getAmount()).isEqualByComparingTo("500.00");
//    }
//
//
//    @Test
//    void updateExpense_throwsIllegalArgumentException_whenDateIsInFuture() {
//        ExpenseRequest request = new ExpenseRequest("Food", new BigDecimal("500.00"), LocalDate.now().plusDays(1), "");
//
//        assertThatThrownBy(() -> expenseService.updateExpense(1L, request)).isInstanceOf(IllegalArgumentException.class);
//
//        verify(expenseRepository, never()).findById(any());
//    }
//
//
//    @Test
//    void updateExpense_throwsResourceNotFoundException_whenExpenseNotFound() {
//        ExpenseRequest request = new ExpenseRequest("Food", new BigDecimal("500.00"), LocalDate.now(), "");
//
//        when(expenseRepository.findById(99L)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> expenseService.updateExpense(99L, request)).isInstanceOf(ResourceNotFoundException.class);
//    }
//
//    //delete
//    @Test
//    void deleteExpense_deletesExpense_whenFound() {
//        when(expenseRepository.findById(1L)).thenReturn(Optional.of(sampleExpense));
//
//        expenseService.deleteExpense(1L);
//
//        verify(expenseRepository, times(1)).delete(sampleExpense);
//    }
//
//    @Test
//    void deleteExpense_throwsResourceNotFoundException_whenNotFound() {
//        when(expenseRepository.findById(99L)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> expenseService.deleteExpense(99L)).isInstanceOf(ResourceNotFoundException.class);
//
//        verify(expenseRepository, never()).delete(any());
//    }
//
//    //getCategorySummary
//    @Test
//    void getCategorySummary_calculatesPercentagePerCategory(){
//        Expense expense1 = new Expense("Food", new BigDecimal("300.00"), LocalDate.now(), "");
//        Expense expense2 = new Expense("Food", new BigDecimal("200.00"), LocalDate.now(), "");
//        Expense expense3 = new Expense("Transport", new BigDecimal("500.00"), LocalDate.now(), "");
//        when(expenseRepository.findAll()).thenReturn(List.of(expense1, expense2, expense3));
//
//        List<CategorySummary> result = expenseService.getCategorySummary();
//
//        CategorySummary food = result.stream().filter(c -> c.getCategory().equals("Food")).findFirst().orElseThrow();
//        assertThat(food.getTotal()).isEqualByComparingTo("500.00");
//        assertThat(food.getPercentage()).isEqualTo(50.0);
//
//    }
//
//    @Test
//    void getCategorySummary_returnsEmptyList_whenNoExpenseFound(){
//        when(expenseRepository.findAll()).thenReturn(List.of());
//
//        List<CategorySummary> result = expenseService.getCategorySummary();
//
//        assertThat(result).isEmpty();
//    }
//
//    //getBurnRate
//    @Test
//    void getBurnRate_flagsOverBudget_whenProjectedSpendExceedsBudget() {
//        YearMonth currentMonth = YearMonth.now();
//        Budget lowBudget = new Budget(currentMonth, new BigDecimal("10.00"));
//        Expense bigExpense = new Expense("Shopping", new BigDecimal("10000.00"), LocalDate.now(), "");
//
//        when(expenseRepository.findAll()).thenReturn(List.of(bigExpense));
//        when(budgetRepository.findByMonth(currentMonth)).thenReturn(Optional.of(lowBudget));
//
//        BurnRateWarning result = expenseService.getBurnRate();
//
//        assertThat(result.isBudgetSet()).isTrue();
//        assertThat(result.isOverBudget()).isTrue();
//    }
//
//    @Test
//    void getBurnRate_flagsBudgetNotSet_whenNoBudgetExistsForCurrentMonth() {
//        YearMonth currentMonth = YearMonth.now();
//
//        when(expenseRepository.findAll()).thenReturn(List.of(sampleExpense));
//        when(budgetRepository.findByMonth(currentMonth)).thenReturn(Optional.empty());
//        BurnRateWarning result = expenseService.getBurnRate();
//
//        assertThat(result.isBudgetSet()).isFalse();
//        assertThat(result.isOverBudget()).isFalse();
//    }
//
//    @Test
//    void getForecast_excludesCurrentInProgressMonth() {
//        YearMonth thisMonth = YearMonth.now();
//        Expense m1 = new Expense("Food", new BigDecimal("300.00"), thisMonth.minusMonths(1).atDay(1), "");
//        Expense m2 = new Expense("Food", new BigDecimal("300.00"), thisMonth.minusMonths(2).atDay(1), "");
//        Expense m3 = new Expense("Food", new BigDecimal("300.00"), thisMonth.minusMonths(3).atDay(1), "");
//        Expense current = new Expense("Food", new BigDecimal("999999.00"), LocalDate.now(), null);
//
//        when(expenseRepository.findAll()).thenReturn(List.of(m1, m2, m3, current));
//
//        List<ForecastResponse> result = expenseService.getForecast();
//        ForecastResponse food = result.stream().filter(f -> f.getCategory().equals("Food")).findFirst().orElseThrow();
//        //will exclude current month
//        assertThat(food.getForecast()).isEqualByComparingTo("300.00");
//    }
//
//    @Test
//    void getForecast_excludesCategoriesWithLessThan3Months() {
//        YearMonth thisMonth = YearMonth.now();
//        Expense onlyOne = new Expense("Rent", new BigDecimal("1000.00"), thisMonth.minusMonths(1).atDay(1), "");
//
//        when(expenseRepository.findAll()).thenReturn(List.of(onlyOne));
//
//        List<ForecastResponse> result = expenseService.getForecast();
//        assertThat(result).isEmpty();
//    }
//
//
//}