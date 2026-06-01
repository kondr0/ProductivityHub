package com.productivityhub.auth.service;

import com.productivityhub.auth.dto.request.LoginRequest;
import com.productivityhub.auth.dto.request.ProfileUpdateRequest;
import com.productivityhub.auth.dto.request.RegisterRequest;
import com.productivityhub.auth.dto.response.AuthResponse;
import com.productivityhub.auth.dto.response.ProfileResponse;
import com.productivityhub.auth.dto.response.ValidationResponse;
import com.productivityhub.auth.entity.Role;
import com.productivityhub.auth.entity.User;
import com.productivityhub.auth.exception.ResourceNotFoundException;
import com.productivityhub.auth.repository.RoleRepository;
import com.productivityhub.auth.repository.UserRepository;
import com.productivityhub.auth.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        Role userRole = roleRepository.findByName(Role.RoleType.ROLE_USER.name())
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name(Role.RoleType.ROLE_USER.name()).build()));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .isEnabled(true)
                .roles(Set.of(userRole))
                .build();

        user = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
        log.info("User registered: {}", user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        if (!user.getIsEnabled()) {
            throw new BadCredentialsException("Account is disabled");
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
        log.info("User logged in: {}", user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .build();
    }

    public ValidationResponse validateToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        boolean valid = jwtTokenProvider.validateToken(token);
        if (valid) {
            return ValidationResponse.builder()
                    .valid(true)
                    .userId(jwtTokenProvider.getUserIdFromToken(token))
                    .email(jwtTokenProvider.getEmailFromToken(token))
                    .build();
        }
        return ValidationResponse.builder().valid(false).build();
    }

    public ProfileResponse getProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .enabled(user.getIsEnabled())
                .build();
    }

    @Transactional
    public ProfileResponse updateProfile(UUID userId, ProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());

        user = userRepository.save(user);

        return ProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .enabled(user.getIsEnabled())
                .build();
    }
}
