package com.productivityhub.auth.controller;

import com.productivityhub.auth.dto.request.LoginRequest;
import com.productivityhub.auth.dto.request.ProfileUpdateRequest;
import com.productivityhub.auth.dto.request.RegisterRequest;
import com.productivityhub.auth.dto.response.AuthResponse;
import com.productivityhub.auth.dto.response.ProfileResponse;
import com.productivityhub.auth.dto.response.ValidationResponse;
import com.productivityhub.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Auth management APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and return JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate JWT token")
    public ResponseEntity<ValidationResponse> validate(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        return ResponseEntity.ok(authService.validateToken(authHeader));
    }

    @GetMapping("/profile")
    @Operation(summary = "Get user profile")
    public ResponseEntity<ProfileResponse> getProfile(@RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(authService.getProfile(userId));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update user profile")
    public ResponseEntity<ProfileResponse> updateProfile(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok(authService.updateProfile(userId, request));
    }
}
