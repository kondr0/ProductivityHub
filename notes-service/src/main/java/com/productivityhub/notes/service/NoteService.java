package com.productivityhub.notes.service;

import com.productivityhub.notes.dto.request.CreateNoteRequest;
import com.productivityhub.notes.dto.request.UpdateNoteRequest;
import com.productivityhub.notes.dto.response.NoteResponse;
import com.productivityhub.notes.entity.NoteTag;
import com.productivityhub.notes.entity.Note;
import com.productivityhub.notes.exception.ResourceNotFoundException;
import com.productivityhub.notes.mapper.NoteMapper;
import com.productivityhub.notes.repository.NoteTagRepository;
import com.productivityhub.notes.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteTagRepository noteTagRepository;
    private final NoteMapper noteMapper;

    public List<NoteResponse> getAllNotes(UUID userId) {
        return noteRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(noteMapper::toResponse).collect(Collectors.toList());
    }

    public NoteResponse getNoteById(UUID id, UUID userId) {
        Note note = noteRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        return noteMapper.toResponse(note);
    }

    public List<NoteResponse> searchNotes(UUID userId, String query) {
        return noteRepository.search(userId, query)
                .stream().map(noteMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public NoteResponse createNote(CreateNoteRequest request, UUID userId) {
        Set<NoteTag> tags = resolveTags(request.getTagIds(), userId);

        Note note = Note.builder()
                .userId(userId)
                .title(request.getTitle())
                .content(request.getContent())
                .isPinned(request.getIsPinned() != null ? request.getIsPinned() : false)
                .tags(tags)
                .build();

        note = noteRepository.save(note);
        log.info("Note created: {} for user {}", note.getId(), userId);
        return noteMapper.toResponse(note);
    }

    @Transactional
    public NoteResponse updateNote(UUID id, UpdateNoteRequest request, UUID userId) {
        Note note = noteRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));

        if (request.getTitle() != null) note.setTitle(request.getTitle());
        if (request.getContent() != null) note.setContent(request.getContent());
        if (request.getIsPinned() != null) note.setIsPinned(request.getIsPinned());
        if (request.getTagIds() != null) {
            note.setTags(resolveTags(request.getTagIds(), userId));
        }

        note = noteRepository.save(note);
        log.info("Note updated: {}", note.getId());
        return noteMapper.toResponse(note);
    }

    @Transactional
    public void deleteNote(UUID id, UUID userId) {
        Note note = noteRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        noteRepository.delete(note);
        log.info("Note deleted: {}", id);
    }

    private Set<NoteTag> resolveTags(Set<UUID> tagIds, UUID userId) {
        if (tagIds == null || tagIds.isEmpty()) return new HashSet<>();
        return tagIds.stream()
                .map(tagId -> noteTagRepository.findById(tagId)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + tagId)))
                .collect(Collectors.toSet());
    }
}
