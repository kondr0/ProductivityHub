package com.productivityhub.moduleregistry.controller;

import com.productivityhub.moduleregistry.service.ModuleRegistryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ModuleRegistryController.class)
class ModuleRegistryControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private ModuleRegistryService moduleRegistryService;

    @Test
    void getModules_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/modules"))
                .andExpect(status().isOk());
    }
}
