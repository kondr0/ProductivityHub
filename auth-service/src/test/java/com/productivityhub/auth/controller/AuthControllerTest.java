package com.productivityhub.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productivityhub.auth.dto.request.LoginRequest;
import com.productivityhub.auth.dto.request.RegisterRequest;
import com.productivityhub.auth.dto.response.AuthResponse;
import com.productivityhub.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private AuthService authService;

    @Test
    void register_ShouldReturn201() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        AuthResponse response = AuthResponse.builder()
                .token("test-token")
                .userId(UUID.randomUUID())
                .email("test@example.com")
                .build();

        when(authService.register(any())).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.token").value("test-token"));
    }

    @Test
    void login_ShouldReturn200() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        AuthResponse response = AuthResponse.builder()
                .token("test-token")
                .userId(UUID.randomUUID())
                .email("test@example.com")
                .build();

        when(authService.login(any())).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
}
