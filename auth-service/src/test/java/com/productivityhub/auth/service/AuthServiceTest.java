package com.productivityhub.auth.service;

import com.productivityhub.auth.dto.request.LoginRequest;
import com.productivityhub.auth.dto.request.RegisterRequest;
import com.productivityhub.auth.dto.response.AuthResponse;
import com.productivityhub.auth.entity.Role;
import com.productivityhub.auth.entity.User;
import com.productivityhub.auth.repository.RoleRepository;
import com.productivityhub.auth.repository.UserRepository;
import com.productivityhub.auth.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtTokenProvider jwtTokenProvider;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, roleRepository, passwordEncoder, jwtTokenProvider);
    }

    @Test
    void register_ShouldReturnAuthResponse() {
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("password123")
                .firstName("Test")
                .lastName("User")
                .build();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(
                Role.builder().id(1L).name("ROLE_USER").build()));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(UUID.randomUUID());
            return user;
        });
        when(jwtTokenProvider.generateToken(any(UUID.class), anyString())).thenReturn("test-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertNotNull(response.getToken());
        assertNotNull(response.getUserId());
    }

    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void login_ShouldReturnAuthResponse() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("encodedPassword")
                .isEnabled(true)
                .roles(Set.of(Role.builder().name("ROLE_USER").build()))
                .build();

        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtTokenProvider.generateToken(any(UUID.class), anyString())).thenReturn("test-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
    }
}
