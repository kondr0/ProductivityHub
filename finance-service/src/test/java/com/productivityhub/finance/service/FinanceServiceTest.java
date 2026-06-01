package com.productivityhub.finance.service;

import com.productivityhub.finance.dto.response.BalanceResponse;
import com.productivityhub.finance.mapper.FinanceMapper;
import com.productivityhub.finance.repository.BudgetRepository;
import com.productivityhub.finance.repository.CategoryRepository;
import com.productivityhub.finance.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinanceServiceTest {

    @Mock private TransactionRepository transactionRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private BudgetRepository budgetRepository;
    @Mock private FinanceMapper financeMapper;

    private FinanceService financeService;

    @BeforeEach
    void setUp() {
        financeService = new FinanceService(transactionRepository, categoryRepository, budgetRepository, financeMapper);
    }

    @Test
    void getBalance_ShouldReturnCorrectBalance() {
        UUID userId = UUID.randomUUID();

        when(transactionRepository.getTotalIncome(userId)).thenReturn(BigDecimal.valueOf(5000));
        when(transactionRepository.getTotalExpense(userId)).thenReturn(BigDecimal.valueOf(3000));

        BalanceResponse balance = financeService.getBalance(userId);

        assertEquals(BigDecimal.valueOf(5000), balance.getIncome());
        assertEquals(BigDecimal.valueOf(3000), balance.getExpense());
        assertEquals(BigDecimal.valueOf(2000), balance.getBalance());
    }
}
