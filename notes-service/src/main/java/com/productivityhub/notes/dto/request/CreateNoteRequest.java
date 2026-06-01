package com.productivityhub.notes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.Set;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CreateNoteRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 256)
    private String title;

    private String content;

    private Boolean isPinned;

    private Set<UUID> tagIds;
}
