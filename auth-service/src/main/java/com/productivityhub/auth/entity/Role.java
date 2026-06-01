package com.productivityhub.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles", schema = "auth")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String name;

    public enum RoleType {
        ROLE_USER, ROLE_ADMIN
    }
}
