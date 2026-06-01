package com.productivityhub.notes.mapper;

import com.productivityhub.notes.dto.response.NoteResponse;
import com.productivityhub.notes.entity.NoteTag;
import com.productivityhub.notes.entity.Note;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class NoteMapper {

    public NoteResponse toResponse(Note note) {
        return NoteResponse.builder()
                .id(note.getId())
                .userId(note.getUserId())
                .title(note.getTitle())
                .content(note.getContent())
                .isPinned(note.getIsPinned())
                .tags(note.getTags() != null ? note.getTags().stream()
                        .map(this::toTagInfo)
                        .collect(Collectors.toSet()) : null)
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }

    private NoteResponse.TagInfo toTagInfo(NoteTag tag) {
        return NoteResponse.TagInfo.builder()
                .id(tag.getId())
                .name(tag.getName())
                .color(tag.getColor())
                .build();
    }
}
