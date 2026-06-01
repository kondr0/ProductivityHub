package com.productivityhub.moduleregistry.service;

import com.productivityhub.moduleregistry.entity.Module;
import com.productivityhub.moduleregistry.entity.UserModule;
import com.productivityhub.moduleregistry.exception.ResourceNotFoundException;
import com.productivityhub.moduleregistry.repository.ModuleRepository;
import com.productivityhub.moduleregistry.repository.UserModuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModuleRegistryService {

    private final ModuleRepository moduleRepository;
    private final UserModuleRepository userModuleRepository;

    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    public List<Map<String, Object>> getUserModules(UUID userId) {
        List<Module> allModules = moduleRepository.findAll();
        List<UserModule> userModules = userModuleRepository.findByUserId(userId);

        Map<UUID, Boolean> userModuleMap = userModules.stream()
                .collect(Collectors.toMap(
                    um -> um.getModule().getId(),
                    UserModule::getEnabled
                ));

        return allModules.stream().map(module -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", module.getId());
            item.put("code", module.getCode());
            item.put("name", module.getName());
            item.put("description", module.getDescription());
            item.put("enabled", userModuleMap.getOrDefault(module.getId(), true));
            return item;
        }).collect(Collectors.toList());
    }

    @Transactional
    public UserModule toggleModule(UUID userId, UUID moduleId, boolean enabled) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found"));

        UserModule userModule = userModuleRepository.findByUserIdAndModuleId(userId, moduleId)
                .orElse(UserModule.builder()
                        .userId(userId)
                        .module(module)
                        .enabled(enabled)
                        .build());

        userModule.setEnabled(enabled);
        userModule = userModuleRepository.save(userModule);
        log.info("Module {} {} for user {}", module.getCode(), enabled ? "enabled" : "disabled", userId);
        return userModule;
    }

    public Map<String, Object> checkModule(String moduleCode, UUID userId) {
        Module module = moduleRepository.findByCode(moduleCode)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found: " + moduleCode));

        UserModule userModule = userModuleRepository.findByUserIdAndModuleCode(userId, moduleCode)
                .orElse(null);

        boolean enabled = userModule == null || userModule.getEnabled();
        Map<String, Object> result = new HashMap<>();
        result.put("module", moduleCode);
        result.put("enabled", enabled);
        result.put("userId", userId.toString());
        return result;
    }
}
