package com.productivityhub.finance.dto.request;

import com.productivityhub.finance.entity.Transaction;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UpdateTransactionRequest {
    private Transaction.TransactionType type;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;
    private UUID categoryId;
}
