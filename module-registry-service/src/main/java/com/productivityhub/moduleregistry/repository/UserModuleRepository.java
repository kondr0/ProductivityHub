package com.productivityhub.moduleregistry.repository;

import com.productivityhub.moduleregistry.entity.UserModule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserModuleRepository extends JpaRepository<UserModule, UUID> {
    List<UserModule> findByUserId(UUID userId);
    Optional<UserModule> findByUserIdAndModuleCode(UUID userId, String moduleCode);
    Optional<UserModule> findByUserIdAndModuleId(UUID userId, UUID moduleId);
}
