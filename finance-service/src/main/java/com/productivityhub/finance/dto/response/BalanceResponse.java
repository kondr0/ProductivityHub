package com.productivityhub.finance.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BalanceResponse {
    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal balance;
}
