package com.productivityhub.dashboard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "finance-service", url = "${services.finance-service}")
public interface FinanceClient {

    @GetMapping("/api/finance/balance")
    Map<String, Object> getBalance(@RequestHeader("X-User-Id") UUID userId);
}
