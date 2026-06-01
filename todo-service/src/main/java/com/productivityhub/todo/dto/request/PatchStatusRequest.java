package com.productivityhub.todo.dto.request;

import com.productivityhub.todo.entity.Task;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PatchStatusRequest {
    @NotNull(message = "Status is required")
    private Task.TaskStatus status;
}
