package com.productivityhub.finance.controller;

import com.productivityhub.finance.dto.StatsResponse;
import com.productivityhub.finance.dto.request.CreateTransactionRequest;
import com.productivityhub.finance.dto.request.UpdateTransactionRequest;
import com.productivityhub.finance.dto.response.BalanceResponse;
import com.productivityhub.finance.dto.response.TransactionResponse;
import com.productivityhub.finance.entity.Budget;
import com.productivityhub.finance.entity.Category;
import com.productivityhub.finance.service.FinanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
@Tag(name = "Finance", description = "Finance management APIs")
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/transactions")
    @Operation(summary = "Get all transactions")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(@RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(financeService.getAllTransactions(userId));
    }

    @PostMapping("/transactions")
    @Operation(summary = "Create a transaction")
    public ResponseEntity<TransactionResponse> createTransaction(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody CreateTransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(financeService.createTransaction(request, userId));
    }

    @GetMapping("/transactions/{id}")
    @Operation(summary = "Get transaction by ID")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(financeService.getTransactionById(id, userId));
    }

    @PutMapping("/transactions/{id}")
    @Operation(summary = "Update transaction")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody UpdateTransactionRequest request) {
        return ResponseEntity.ok(financeService.updateTransaction(id, request, userId));
    }

    @DeleteMapping("/transactions/{id}")
    @Operation(summary = "Delete transaction")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId) {
        financeService.deleteTransaction(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/balance")
    @Operation(summary = "Get current balance")
    public ResponseEntity<BalanceResponse> getBalance(@RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(financeService.getBalance(userId));
    }

    @GetMapping("/stats")
    @Operation(summary = "Get statistics for period")
    public ResponseEntity<StatsResponse> getStats(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return ResponseEntity.ok(financeService.getStats(userId, start, end));
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all categories")
    public ResponseEntity<List<Category>> getCategories(@RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(financeService.getCategories(userId));
    }

    @PostMapping("/categories")
    @Operation(summary = "Create a category")
    public ResponseEntity<Category> createCategory(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody Category category) {
        category.setUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(financeService.createCategory(category));
    }

    @PutMapping("/categories/{id}")
    @Operation(summary = "Update category")
    public ResponseEntity<Category> updateCategory(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestBody Category category) {
        return ResponseEntity.ok(financeService.updateCategory(id, userId, category));
    }

    @DeleteMapping("/categories/{id}")
    @Operation(summary = "Delete category")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId) {
        financeService.deleteCategory(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/budgets")
    @Operation(summary = "Get all budgets")
    public ResponseEntity<List<Budget>> getBudgets(@RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(financeService.getBudgets(userId));
    }

    @PostMapping("/budgets")
    @Operation(summary = "Create a budget")
    public ResponseEntity<Budget> createBudget(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody Budget budget) {
        budget.setUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(financeService.createBudget(budget));
    }

    @PutMapping("/budgets/{id}")
    @Operation(summary = "Update budget")
    public ResponseEntity<Budget> updateBudget(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestBody Budget budget) {
        return ResponseEntity.ok(financeService.updateBudget(id, userId, budget));
    }

    @DeleteMapping("/budgets/{id}")
    @Operation(summary = "Delete budget")
    public ResponseEntity<Void> deleteBudget(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId) {
        financeService.deleteBudget(id, userId);
        return ResponseEntity.noContent().build();
    }
}
