package com.productivityhub.auth.dto.response;

import lombok.*;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private UUID userId;
    private String email;
}
