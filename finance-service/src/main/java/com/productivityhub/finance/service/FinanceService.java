package com.productivityhub.finance.service;

import com.productivityhub.finance.dto.StatsResponse;
import com.productivityhub.finance.dto.request.CreateTransactionRequest;
import com.productivityhub.finance.dto.request.UpdateTransactionRequest;
import com.productivityhub.finance.dto.response.BalanceResponse;
import com.productivityhub.finance.dto.response.TransactionResponse;
import com.productivityhub.finance.entity.Budget;
import com.productivityhub.finance.entity.Category;
import com.productivityhub.finance.entity.Transaction;
import com.productivityhub.finance.exception.ResourceNotFoundException;
import com.productivityhub.finance.mapper.FinanceMapper;
import com.productivityhub.finance.repository.BudgetRepository;
import com.productivityhub.finance.repository.CategoryRepository;
import com.productivityhub.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetRepository budgetRepository;
    private final FinanceMapper financeMapper;

    public List<TransactionResponse> getAllTransactions(UUID userId) {
        return transactionRepository.findByUserIdOrderByTransactionDateDesc(userId)
                .stream().map(financeMapper::toResponse).collect(Collectors.toList());
    }

    public TransactionResponse getTransactionById(UUID id, UUID userId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        return financeMapper.toResponse(transaction);
    }

    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest request, UUID userId) {
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }

        Transaction transaction = Transaction.builder()
                .userId(userId)
                .type(request.getType())
                .amount(request.getAmount())
                .description(request.getDescription())
                .transactionDate(request.getTransactionDate())
                .category(category)
                .build();

        transaction = transactionRepository.save(transaction);
        log.info("Transaction created: {} for user {}", transaction.getId(), userId);
        return financeMapper.toResponse(transaction);
    }

    @Transactional
    public TransactionResponse updateTransaction(UUID id, UpdateTransactionRequest request, UUID userId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (request.getType() != null) transaction.setType(request.getType());
        if (request.getAmount() != null) transaction.setAmount(request.getAmount());
        if (request.getDescription() != null) transaction.setDescription(request.getDescription());
        if (request.getTransactionDate() != null) transaction.setTransactionDate(request.getTransactionDate());
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            transaction.setCategory(category);
        }

        transaction = transactionRepository.save(transaction);
        log.info("Transaction updated: {}", transaction.getId());
        return financeMapper.toResponse(transaction);
    }

    @Transactional
    public void deleteTransaction(UUID id, UUID userId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        transactionRepository.delete(transaction);
        log.info("Transaction deleted: {}", id);
    }

    public BalanceResponse getBalance(UUID userId) {
        BigDecimal income = transactionRepository.getTotalIncome(userId);
        BigDecimal expense = transactionRepository.getTotalExpense(userId);
        return BalanceResponse.builder()
                .income(income)
                .expense(expense)
                .balance(income.subtract(expense))
                .build();
    }

    public StatsResponse getStats(UUID userId, LocalDate start, LocalDate end) {
        BigDecimal income = transactionRepository.getTotalByTypeAndPeriod(userId, Transaction.TransactionType.INCOME, start, end);
        BigDecimal expense = transactionRepository.getTotalByTypeAndPeriod(userId, Transaction.TransactionType.EXPENSE, start, end);

        List<Object[]> byCategory = transactionRepository.getExpenseByCategory(userId, start, end);
        Map<String, BigDecimal> categoryMap = new HashMap<>();
        for (Object[] row : byCategory) {
            UUID catId = (UUID) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            categoryRepository.findById(catId).ifPresent(cat -> categoryMap.put(cat.getName(), amount));
        }

        return StatsResponse.builder()
                .totalIncome(income)
                .totalExpense(expense)
                .byCategory(categoryMap)
                .build();
    }

    public List<Category> getCategories(UUID userId) {
        return categoryRepository.findByUserIdOrderByName(userId);
    }

    @Transactional
    public Category createCategory(Category category) {
        category = categoryRepository.save(category);
        log.info("Category created: {}", category.getName());
        return category;
    }

    @Transactional
    public Category updateCategory(UUID id, UUID userId, Category updated) {
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (updated.getName() != null) category.setName(updated.getName());
        if (updated.getColor() != null) category.setColor(updated.getColor());
        if (updated.getIcon() != null) category.setIcon(updated.getIcon());
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(UUID id, UUID userId) {
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        categoryRepository.delete(category);
        log.info("Category deleted: {}", id);
    }

    public List<Budget> getBudgets(UUID userId) {
        return budgetRepository.findByUserId(userId);
    }

    @Transactional
    public Budget createBudget(Budget budget) {
        budget = budgetRepository.save(budget);
        log.info("Budget created for user {}", budget.getUserId());
        return budget;
    }

    @Transactional
    public Budget updateBudget(UUID id, UUID userId, Budget updated) {
        Budget budget = budgetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
        if (updated.getAmount() != null) budget.setAmount(updated.getAmount());
        if (updated.getPeriod() != null) budget.setPeriod(updated.getPeriod());
        if (updated.getStartDate() != null) budget.setStartDate(updated.getStartDate());
        if (updated.getEndDate() != null) budget.setEndDate(updated.getEndDate());
        if (updated.getCategory() != null) budget.setCategory(updated.getCategory());
        return budgetRepository.save(budget);
    }

    @Transactional
    public void deleteBudget(UUID id, UUID userId) {
        Budget budget = budgetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
        budgetRepository.delete(budget);
        log.info("Budget deleted: {}", id);
    }
}
