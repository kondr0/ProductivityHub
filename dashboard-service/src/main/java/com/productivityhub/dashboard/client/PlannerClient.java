package com.productivityhub.dashboard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "planner-service", url = "${services.planner-service}")
public interface PlannerClient {

    @GetMapping("/api/planner/events/count")
    Map<String, Object> getEventsCount(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestParam("date") LocalDate date);
}
