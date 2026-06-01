package com.productivityhub.todo.controller;

import com.productivityhub.todo.dto.request.CreateTodoRequest;
import com.productivityhub.todo.dto.request.PatchStatusRequest;
import com.productivityhub.todo.dto.request.UpdateTodoRequest;
import com.productivityhub.todo.dto.response.TodoResponse;
import com.productivityhub.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Tag(name = "To-Do", description = "Task management APIs")
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    @Operation(summary = "Get all tasks for user with optional filters")
    public ResponseEntity<List<TodoResponse>> getAllTodos(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate) {
        return ResponseEntity.ok(todoService.getAllTodos(userId, status, priority, tag, dueDate));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID")
    public ResponseEntity<TodoResponse> getTodoById(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(todoService.getTodoById(id, userId));
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<TodoResponse> createTodo(
            @Valid @RequestBody CreateTodoRequest request,
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(request, userId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task by ID")
    public ResponseEntity<TodoResponse> updateTodo(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTodoRequest request,
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(todoService.updateTodo(id, request, userId));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update task status only")
    public ResponseEntity<TodoResponse> patchStatus(
            @PathVariable UUID id,
            @Valid @RequestBody PatchStatusRequest request,
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(todoService.patchStatus(id, request, userId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task by ID")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") UUID userId) {
        todoService.deleteTodo(id, userId);
        return ResponseEntity.noContent().build();
    }
}
