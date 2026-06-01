package com.productivityhub.todo.mapper;

import com.productivityhub.todo.dto.response.TodoResponse;
import com.productivityhub.todo.entity.Tag;
import com.productivityhub.todo.entity.Task;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TodoMapper {

    public TodoResponse toResponse(Task task) {
        return TodoResponse.builder()
                .id(task.getId())
                .userId(task.getUserId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .tags(task.getTags() != null ? task.getTags().stream()
                        .map(this::toTagInfo)
                        .collect(Collectors.toSet()) : null)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

    private TodoResponse.TagInfo toTagInfo(Tag tag) {
        return TodoResponse.TagInfo.builder()
                .id(tag.getId())
                .name(tag.getName())
                .color(tag.getColor())
                .build();
    }
}
