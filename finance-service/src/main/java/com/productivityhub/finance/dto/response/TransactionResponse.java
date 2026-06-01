package com.productivityhub.finance.dto.response;

import com.productivityhub.finance.entity.Transaction;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TransactionResponse {
    private UUID id;
    private UUID userId;
    private Transaction.TransactionType type;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;
    private CategoryInfo category;
    private LocalDateTime createdAt;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class CategoryInfo {
        private UUID id;
        private String name;
        private String color;
    }
}
