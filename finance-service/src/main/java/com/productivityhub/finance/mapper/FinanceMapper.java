package com.productivityhub.finance.mapper;

import com.productivityhub.finance.dto.response.TransactionResponse;
import com.productivityhub.finance.entity.Category;
import com.productivityhub.finance.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class FinanceMapper {

    public TransactionResponse toResponse(Transaction transaction) {
        TransactionResponse response = TransactionResponse.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .createdAt(transaction.getCreatedAt())
                .build();

        if (transaction.getCategory() != null) {
            response.setCategory(TransactionResponse.CategoryInfo.builder()
                    .id(transaction.getCategory().getId())
                    .name(transaction.getCategory().getName())
                    .color(transaction.getCategory().getColor())
                    .build());
        }

        return response;
    }
}
