package com.productivityhub.notes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productivityhub.notes.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private NoteService noteService;

    @Test
    void getNotes_ShouldReturn200() throws Exception {
        UUID userId = UUID.randomUUID();
        mockMvc.perform(get("/api/notes")
                .header("X-User-Id", userId.toString()))
                .andExpect(status().isOk());
    }
}
