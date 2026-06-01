package com.productivityhub.dashboard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "todo-service", url = "${services.todo-service}")
public interface TodoClient {

    @GetMapping("/api/todos/stats")
    Map<String, Object> getTodoStats(@RequestHeader("X-User-Id") UUID userId);
}
