package com.productivityhub.notes.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class NoteResponse {
    private UUID id;
    private UUID userId;
    private String title;
    private String content;
    private Boolean isPinned;
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
