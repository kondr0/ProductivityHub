package com.productivityhub.dashboard.repository;

import com.productivityhub.dashboard.entity.DashboardConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface DashboardConfigRepository extends JpaRepository<DashboardConfig, UUID> {
    Optional<DashboardConfig> findByUserId(UUID userId);
}
