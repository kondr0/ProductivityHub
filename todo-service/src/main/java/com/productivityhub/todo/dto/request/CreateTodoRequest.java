package com.productivityhub.todo.dto.request;

import com.productivityhub.todo.entity.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CreateTodoRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 256)
    private String title;

    private String description;

    private Task.Priority priority;

    private LocalDate dueDate;

    private Set<UUID> tagIds;
}
