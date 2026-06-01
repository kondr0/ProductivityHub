package com.productivityhub.planner.controller;

import com.productivityhub.planner.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private EventService eventService;

    @Test
    void getEvents_ShouldReturn200() throws Exception {
        UUID userId = UUID.randomUUID();
        mockMvc.perform(get("/api/planner/events")
                .header("X-User-Id", userId.toString()))
                .andExpect(status().isOk());
    }
}
