package com.productivityhub.dashboard.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "dashboard_config", schema = "dashboard")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DashboardConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(columnDefinition = "TEXT")
    private String layout;

    @Column(name = "widgets_json", columnDefinition = "TEXT")
    private String widgetsJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
