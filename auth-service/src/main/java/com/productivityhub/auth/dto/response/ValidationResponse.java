package com.productivityhub.auth.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ValidationResponse {
    private boolean valid;
    private String userId;
    private String email;
}
