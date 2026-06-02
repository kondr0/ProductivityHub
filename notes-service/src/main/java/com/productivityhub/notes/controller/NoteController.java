package com.productivityhub.notes.controller;

import com.productivityhub.notes.dto.request.CreateNoteRequest;
import com.productivityhub.notes.dto.request.UpdateNoteRequest;
import com.productivityhub.notes.dto.response.NoteResponse;
import com.productivityhub.notes.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
@Tag(name = "Notes", description = "Notes management APIs")
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    @Operation(summary = "Get all notes")
    public ResponseEntity<List<NoteResponse>> getAllNotes(@RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(noteService.getAllNotes(userId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get note by ID")
    public ResponseEntity<NoteResponse> getNoteById(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(noteService.getNoteById(id, userId));
    }

    @PostMapping
    @Operation(summary = "Create a note")
    public ResponseEntity<NoteResponse> createNote(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody CreateNoteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.createNote(request, userId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a note")
    public ResponseEntity<NoteResponse> updateNote(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody UpdateNoteRequest request) {
        return ResponseEntity.ok(noteService.updateNote(id, request, userId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a note")
    public ResponseEntity<Void> deleteNote(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId) {
        noteService.deleteNote(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    @Operation(summary = "Get notes count for user")
    public ResponseEntity<Map<String, Long>> getNotesCount(@RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(Map.of("count", noteService.getNotesCount(userId)));
    }

    @GetMapping("/search")
    @Operation(summary = "Search notes")
    public ResponseEntity<List<NoteResponse>> searchNotes(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestParam("q") String query) {
        return ResponseEntity.ok(noteService.searchNotes(userId, query));
    }
}
