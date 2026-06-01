package com.productivityhub.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productivityhub.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private TodoService todoService;

    @Test
    void getTodos_ShouldReturn200() throws Exception {
        UUID userId = UUID.randomUUID();
        mockMvc.perform(get("/api/todos")
                .header("X-User-Id", userId.toString()))
                .andExpect(status().isOk());
    }
}
