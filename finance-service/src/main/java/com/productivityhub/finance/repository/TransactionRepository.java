package com.productivityhub.finance.repository;

import com.productivityhub.finance.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByUserIdOrderByTransactionDateDesc(UUID userId);

    Optional<Transaction> findByIdAndUserId(UUID id, UUID userId);

    List<Transaction> findByUserIdAndTransactionDateBetweenOrderByTransactionDateDesc(
            UUID userId, LocalDate start, LocalDate end);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.userId = :userId AND t.type = 'INCOME'")
    BigDecimal getTotalIncome(@Param("userId") UUID userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.userId = :userId AND t.type = 'EXPENSE'")
    BigDecimal getTotalExpense(@Param("userId") UUID userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.userId = :userId " +
           "AND t.type = :type AND t.transactionDate BETWEEN :start AND :end")
    BigDecimal getTotalByTypeAndPeriod(
            @Param("userId") UUID userId,
            @Param("type") Transaction.TransactionType type,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);

    @Query("SELECT t.category.id, COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.userId = :userId AND t.type = 'EXPENSE' " +
           "AND t.transactionDate BETWEEN :start AND :end " +
           "GROUP BY t.category.id")
    List<Object[]> getExpenseByCategory(
            @Param("userId") UUID userId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);

    void deleteByIdAndUserId(UUID id, UUID userId);
}
