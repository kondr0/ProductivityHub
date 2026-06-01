package com.productivityhub.dashboard.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "notes-service", url = "${services.notes-service}")
public interface NotesClient {

    @GetMapping("/api/notes/count")
    Map<String, Object> getNotesCount(@RequestHeader("X-User-Id") UUID userId);
}
