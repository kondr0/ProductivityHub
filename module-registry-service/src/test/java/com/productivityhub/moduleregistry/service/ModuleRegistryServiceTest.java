package com.productivityhub.moduleregistry.service;

import com.productivityhub.moduleregistry.entity.Module;
import com.productivityhub.moduleregistry.repository.ModuleRepository;
import com.productivityhub.moduleregistry.repository.UserModuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleRegistryServiceTest {

    @Mock private ModuleRepository moduleRepository;
    @Mock private UserModuleRepository userModuleRepository;

    private ModuleRegistryService moduleRegistryService;

    @BeforeEach
    void setUp() {
        moduleRegistryService = new ModuleRegistryService(moduleRepository, userModuleRepository);
    }

    @Test
    void getAllModules_ShouldReturnList() {
        when(moduleRepository.findAll()).thenReturn(List.of(
                Module.builder().id(UUID.randomUUID()).code("todo").name("Tasks").build()
        ));

        List<Module> modules = moduleRegistryService.getAllModules();
        assertEquals(1, modules.size());
        assertEquals("todo", modules.get(0).getCode());
    }
}
