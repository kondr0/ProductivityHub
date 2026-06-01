package com.productivityhub.moduleregistry.controller;

import com.productivityhub.moduleregistry.entity.Module;
import com.productivityhub.moduleregistry.entity.UserModule;
import com.productivityhub.moduleregistry.service.ModuleRegistryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
@Tag(name = "Module Registry", description = "Module management APIs")
public class ModuleRegistryController {

    private final ModuleRegistryService moduleRegistryService;

    @GetMapping
    @Operation(summary = "Get all available modules")
    public ResponseEntity<List<Module>> getAllModules() {
        return ResponseEntity.ok(moduleRegistryService.getAllModules());
    }

    @GetMapping("/user")
    @Operation(summary = "Get user modules with status")
    public ResponseEntity<List<Map<String, Object>>> getUserModules(@RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(moduleRegistryService.getUserModules(userId));
    }

    @PutMapping("/user/{moduleId}")
    @Operation(summary = "Toggle module for user")
    public ResponseEntity<UserModule> toggleModule(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable UUID moduleId,
            @RequestBody Map<String, Boolean> body) {
        return ResponseEntity.ok(moduleRegistryService.toggleModule(userId, moduleId, body.getOrDefault("enabled", true)));
    }

    @GetMapping("/user/check")
    @Operation(summary = "Check if module is enabled for user (used by gateway)")
    public ResponseEntity<Map<String, Object>> checkModule(
            @RequestParam("module") String moduleCode,
            @RequestParam("userId") UUID userId) {
        return ResponseEntity.ok(moduleRegistryService.checkModule(moduleCode, userId));
    }
}
