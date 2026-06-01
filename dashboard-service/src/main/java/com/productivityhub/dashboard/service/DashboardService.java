package com.productivityhub.dashboard.service;

import com.productivityhub.dashboard.client.*;
import com.productivityhub.dashboard.dto.DashboardResponse;
import com.productivityhub.dashboard.entity.DashboardConfig;
import com.productivityhub.dashboard.repository.DashboardConfigRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TodoClient todoClient;
    private final FinanceClient financeClient;
    private final NotesClient notesClient;
    private final PlannerClient plannerClient;
    private final DashboardConfigRepository configRepository;
    private final ObjectMapper objectMapper;

    public DashboardResponse getDashboard(UUID userId) {
        DashboardResponse response = new DashboardResponse();
        response.setUserId(userId);
        response.setWidgets(new ArrayList<>());

        try {
            Map<String, Object> todoStats = todoClient.getTodoStats(userId);
            response.getWidgets().add(DashboardResponse.WidgetData.builder()
                    .id("widget-todo")
                    .type("todo_stats")
                    .title("Мои задачи")
                    .data(todoStats)
                    .build());
        } catch (Exception e) {
            log.error("Failed to fetch todo stats", e);
        }

        try {
            Map<String, Object> balance = financeClient.getBalance(userId);
            response.getWidgets().add(DashboardResponse.WidgetData.builder()
                    .id("widget-finance")
                    .type("finance_balance")
                    .title("Баланс")
                    .data(balance)
                    .build());
        } catch (Exception e) {
            log.error("Failed to fetch finance balance", e);
        }

        try {
            Map<String, Object> notesCount = notesClient.getNotesCount(userId);
            response.getWidgets().add(DashboardResponse.WidgetData.builder()
                    .id("widget-notes")
                    .type("notes_recent")
                    .title("Заметки")
                    .data(notesCount)
                    .build());
        } catch (Exception e) {
            log.error("Failed to fetch notes", e);
        }

        try {
            Map<String, Object> eventsCount = plannerClient.getEventsCount(userId, LocalDate.now());
            response.getWidgets().add(DashboardResponse.WidgetData.builder()
                    .id("widget-planner")
                    .type("planner_today")
                    .title("Сегодня")
                    .data(eventsCount)
                    .build());
        } catch (Exception e) {
            log.error("Failed to fetch planner events", e);
        }

        return response;
    }

    public DashboardConfig getConfig(UUID userId) {
        return configRepository.findByUserId(userId)
                .orElse(null);
    }

    public DashboardConfig updateConfig(UUID userId, DashboardConfig newConfig) {
        DashboardConfig config = configRepository.findByUserId(userId)
                .orElse(DashboardConfig.builder().userId(userId).build());
        if (newConfig.getLayout() != null) config.setLayout(newConfig.getLayout());
        if (newConfig.getWidgetsJson() != null) config.setWidgetsJson(newConfig.getWidgetsJson());
        return configRepository.save(config);
    }
}
