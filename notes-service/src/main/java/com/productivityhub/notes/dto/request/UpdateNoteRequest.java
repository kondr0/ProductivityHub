package com.productivityhub.notes.dto.request;

import lombok.*;
import java.util.Set;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UpdateNoteRequest {
    private String title;
    private String content;
    private Boolean isPinned;
    private Set<UUID> tagIds;
}
