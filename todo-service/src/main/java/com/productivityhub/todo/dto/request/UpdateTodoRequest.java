package com.productivityhub.todo.dto.request;

import com.productivityhub.todo.entity.Task;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UpdateTodoRequest {
    private String title;
    private String description;
    private Task.TaskStatus status;
    private Task.Priority priority;
    private LocalDate dueDate;
    private Set<UUID> tagIds;
}
