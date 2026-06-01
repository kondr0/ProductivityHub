package com.productivityhub.planner.controller;

import com.productivityhub.planner.dto.request.CreateEventRequest;
import com.productivityhub.planner.dto.request.UpdateEventRequest;
import com.productivityhub.planner.dto.response.EventResponse;
import com.productivityhub.planner.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/planner")
@RequiredArgsConstructor
@Tag(name = "Planner", description = "Event planning APIs")
public class EventController {

    private final EventService eventService;

    @GetMapping("/events")
    @Operation(summary = "Get all events with optional date range")
    public ResponseEntity<List<EventResponse>> getAllEvents(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end,
            @RequestParam(required = false) LocalDate date) {
        if (date != null) {
            return ResponseEntity.ok(eventService.getEventsByDate(userId, date));
        }
        return ResponseEntity.ok(eventService.getAllEvents(userId, start, end));
    }

    @GetMapping("/events/{id}")
    @Operation(summary = "Get event by ID")
    public ResponseEntity<EventResponse> getEventById(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(eventService.getEventById(id, userId));
    }

    @PostMapping("/events")
    @Operation(summary = "Create an event")
    public ResponseEntity<EventResponse> createEvent(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody CreateEventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request, userId));
    }

    @PutMapping("/events/{id}")
    @Operation(summary = "Update an event")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody UpdateEventRequest request) {
        return ResponseEntity.ok(eventService.updateEvent(id, request, userId));
    }

    @DeleteMapping("/events/{id}")
    @Operation(summary = "Delete an event")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId) {
        eventService.deleteEvent(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/events/count")
    @Operation(summary = "Get events count for a date")
    public ResponseEntity<Map<String, Object>> getEventsCount(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestParam("date") LocalDate date) {
        long count = eventService.getEventsCount(userId, date);
        return ResponseEntity.ok(Map.of("count", count, "date", date.toString()));
    }
}
