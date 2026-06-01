package com.productivityhub.finance.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class StatsResponse {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private Map<String, BigDecimal> byCategory;
}
