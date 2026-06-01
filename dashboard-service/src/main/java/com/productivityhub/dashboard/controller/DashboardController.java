package com.productivityhub.dashboard.controller;

import com.productivityhub.dashboard.dto.DashboardResponse;
import com.productivityhub.dashboard.entity.DashboardConfig;
import com.productivityhub.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard aggregation APIs")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Get aggregated dashboard data")
    public ResponseEntity<DashboardResponse> getDashboard(@RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(dashboardService.getDashboard(userId));
    }

    @GetMapping("/config")
    @Operation(summary = "Get dashboard config")
    public ResponseEntity<DashboardConfig> getConfig(@RequestHeader("X-User-Id") UUID userId) {
        DashboardConfig config = dashboardService.getConfig(userId);
        if (config == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(config);
    }

    @PutMapping("/config")
    @Operation(summary = "Update dashboard config")
    public ResponseEntity<DashboardConfig> updateConfig(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestBody DashboardConfig config) {
        return ResponseEntity.ok(dashboardService.updateConfig(userId, config));
    }
}
