package com.productivityhub.finance.dto.request;

import com.productivityhub.finance.entity.Transaction;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CreateTransactionRequest {
    @NotNull(message = "Type is required")
    private Transaction.TransactionType type;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private String description;

    @NotNull(message = "Transaction date is required")
    private LocalDate transactionDate;

    private UUID categoryId;
}
