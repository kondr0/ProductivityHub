package com.productivityhub.todo.dto.response;

import com.productivityhub.todo.entity.Task;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TodoResponse {
    private UUID id;
    private UUID userId;
    private String title;
    private String description;
    private Task.TaskStatus status;
    private Task.Priority priority;
    private LocalDate dueDate;
    private Set<TagInfo> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class TagInfo {
        private UUID id;
        private String name;
        private String color;
    }
}
