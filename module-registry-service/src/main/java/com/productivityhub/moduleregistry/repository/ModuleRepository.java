package com.productivityhub.moduleregistry.repository;

import com.productivityhub.moduleregistry.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<Module, UUID> {
    Optional<Module> findByCode(String code);
}
