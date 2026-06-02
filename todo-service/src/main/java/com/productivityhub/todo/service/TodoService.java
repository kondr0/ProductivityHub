package com.productivityhub.todo.service;

import com.productivityhub.todo.dto.request.CreateTodoRequest;
import com.productivityhub.todo.dto.request.PatchStatusRequest;
import com.productivityhub.todo.dto.request.UpdateTodoRequest;
import com.productivityhub.todo.dto.response.TodoResponse;
import com.productivityhub.todo.entity.Tag;
import com.productivityhub.todo.entity.Task;
import com.productivityhub.todo.exception.ResourceNotFoundException;
import com.productivityhub.todo.mapper.TodoMapper;
import com.productivityhub.todo.repository.TagRepository;
import com.productivityhub.todo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TaskRepository taskRepository;
    private final TagRepository tagRepository;
    private final TodoMapper todoMapper;

    public List<TodoResponse> getAllTodos(UUID userId, String status, String priority, String tag, LocalDate dueDate) {
        List<Task> tasks;

        if (status != null) {
            tasks = taskRepository.findByUserIdAndStatus(userId, Task.TaskStatus.valueOf(status.toUpperCase()));
        } else if (priority != null) {
            tasks = taskRepository.findByUserIdAndPriority(userId, Task.Priority.valueOf(priority.toUpperCase()));
        } else if (tag != null) {
            UUID tagId = UUID.fromString(tag);
            tasks = taskRepository.findByUserIdAndTagId(userId, tagId);
        } else if (dueDate != null) {
            tasks = taskRepository.findByUserIdAndDueDate(userId, dueDate);
        } else {
            tasks = taskRepository.findByUserIdOrderByCreatedAtDesc(userId);
        }

        return tasks.stream().map(todoMapper::toResponse).collect(Collectors.toList());
    }

    public TodoResponse getTodoById(UUID id, UUID userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return todoMapper.toResponse(task);
    }

    @Transactional
    public TodoResponse createTodo(CreateTodoRequest request, UUID userId) {
        Set<Tag> tags = resolveTags(request.getTagIds(), userId);

        Task task = Task.builder()
                .userId(userId)
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority() != null ? request.getPriority() : Task.Priority.MEDIUM)
                .dueDate(request.getDueDate())
                .tags(tags)
                .build();

        task = taskRepository.save(task);
        log.info("Task created: {} for user {}", task.getId(), userId);
        return todoMapper.toResponse(task);
    }

    @Transactional
    public TodoResponse updateTodo(UUID id, UpdateTodoRequest request, UUID userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
        if (request.getTagIds() != null) {
            task.setTags(resolveTags(request.getTagIds(), userId));
        }

        task = taskRepository.save(task);
        log.info("Task updated: {}", task.getId());
        return todoMapper.toResponse(task);
    }

    @Transactional
    public TodoResponse patchStatus(UUID id, PatchStatusRequest request, UUID userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        task.setStatus(request.getStatus());
        task = taskRepository.save(task);
        log.info("Task status updated: {} -> {}", task.getId(), request.getStatus());
        return todoMapper.toResponse(task);
    }

    @Transactional
    public void deleteTodo(UUID id, UUID userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        taskRepository.delete(task);
        log.info("Task deleted: {}", id);
    }

    public Map<String, Long> getTodoStats(UUID userId) {
        long total = taskRepository.countByUserId(userId);
        long todo = taskRepository.countByUserIdAndStatus(userId, Task.TaskStatus.TODO);
        long inProgress = taskRepository.countByUserIdAndStatus(userId, Task.TaskStatus.IN_PROGRESS);
        long done = taskRepository.countByUserIdAndStatus(userId, Task.TaskStatus.DONE);
        long overdue = taskRepository.countByUserIdAndDueDateBeforeAndStatusNot(
                userId, LocalDate.now(), Task.TaskStatus.DONE);

        return Map.of(
            "total", total,
            "todo", todo,
            "inProgress", inProgress,
            "done", done,
            "overdue", overdue
        );
    }

    private Set<Tag> resolveTags(Set<UUID> tagIds, UUID userId) {
        if (tagIds == null || tagIds.isEmpty()) return new HashSet<>();
        return tagIds.stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + tagId)))
                .collect(Collectors.toSet());
    }
}
