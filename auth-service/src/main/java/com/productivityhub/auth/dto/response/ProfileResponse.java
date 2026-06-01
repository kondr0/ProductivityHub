package com.productivityhub.auth.dto.response;

import lombok.*;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProfileResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enabled;
}
