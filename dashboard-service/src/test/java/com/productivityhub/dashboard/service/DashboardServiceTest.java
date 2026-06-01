package com.productivityhub.dashboard.service;

import com.productivityhub.dashboard.client.*;
import com.productivityhub.dashboard.repository.DashboardConfigRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock private TodoClient todoClient;
    @Mock private FinanceClient financeClient;
    @Mock private NotesClient notesClient;
    @Mock private PlannerClient plannerClient;
    @Mock private DashboardConfigRepository configRepository;
    @Mock private ObjectMapper objectMapper;

    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        dashboardService = new DashboardService(todoClient, financeClient, notesClient, plannerClient, configRepository, objectMapper);
    }

    @Test
    void getDashboard_ShouldReturnData() {
        UUID userId = UUID.randomUUID();
        var result = dashboardService.getDashboard(userId);
        org.junit.jupiter.api.Assertions.assertNotNull(result);
        org.junit.jupiter.api.Assertions.assertEquals(userId, result.getUserId());
    }
}
