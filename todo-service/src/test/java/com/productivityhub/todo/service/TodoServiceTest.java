package com.productivityhub.todo.service;

import com.productivityhub.todo.dto.request.CreateTodoRequest;
import com.productivityhub.todo.dto.response.TodoResponse;
import com.productivityhub.todo.entity.Tag;
import com.productivityhub.todo.entity.Task;
import com.productivityhub.todo.mapper.TodoMapper;
import com.productivityhub.todo.repository.TagRepository;
import com.productivityhub.todo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock private TaskRepository taskRepository;
    @Mock private TagRepository tagRepository;
    @Mock private TodoMapper todoMapper;

    private TodoService todoService;

    @BeforeEach
    void setUp() {
        todoService = new TodoService(taskRepository, tagRepository, todoMapper);
    }

    @Test
    void getTodoById_ShouldReturnTodoResponse() {
        UUID taskId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Task task = Task.builder().id(taskId).userId(userId).title("Test").build();
        TodoResponse response = TodoResponse.builder().id(taskId).title("Test").build();

        when(taskRepository.findByIdAndUserId(taskId, userId)).thenReturn(Optional.of(task));
        when(todoMapper.toResponse(task)).thenReturn(response);

        TodoResponse result = todoService.getTodoById(taskId, userId);

        assertNotNull(result);
        assertEquals("Test", result.getTitle());
    }

    @Test
    void getTodoById_ShouldThrowException_WhenNotFound() {
        UUID taskId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(taskRepository.findByIdAndUserId(taskId, userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> todoService.getTodoById(taskId, userId));
    }
}
