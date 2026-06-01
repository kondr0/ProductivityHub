package com.productivityhub.auth.dto.request;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProfileUpdateRequest {
    private String firstName;
    private String lastName;
}
