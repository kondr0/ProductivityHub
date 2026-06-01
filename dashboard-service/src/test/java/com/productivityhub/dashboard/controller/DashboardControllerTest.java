package com.productivityhub.dashboard.controller;

import com.productivityhub.dashboard.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private DashboardService dashboardService;

    @Test
    void getDashboard_ShouldReturn200() throws Exception {
        UUID userId = UUID.randomUUID();
        mockMvc.perform(get("/api/dashboard")
                .header("X-User-Id", userId.toString()))
                .andExpect(status().isOk());
    }
}
