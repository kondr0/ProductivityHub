package com.productivityhub.finance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productivityhub.finance.dto.response.BalanceResponse;
import com.productivityhub.finance.service.FinanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FinanceController.class)
class FinanceControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private FinanceService financeService;

    @Test
    void getBalance_ShouldReturnBalance() throws Exception {
        UUID userId = UUID.randomUUID();
        BalanceResponse response = BalanceResponse.builder()
                .income(BigDecimal.valueOf(5000))
                .expense(BigDecimal.valueOf(3000))
                .balance(BigDecimal.valueOf(2000))
                .build();

        when(financeService.getBalance(any())).thenReturn(response);

        mockMvc.perform(get("/api/finance/balance")
                .header("X-User-Id", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(2000));
    }
}
