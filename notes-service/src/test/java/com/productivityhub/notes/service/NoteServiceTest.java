package com.productivityhub.notes.service;

import com.productivityhub.notes.dto.response.NoteResponse;
import com.productivityhub.notes.entity.Note;
import com.productivityhub.notes.mapper.NoteMapper;
import com.productivityhub.notes.repository.NoteTagRepository;
import com.productivityhub.notes.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock private NoteRepository noteRepository;
    @Mock private NoteTagRepository noteTagRepository;
    @Mock private NoteMapper noteMapper;

    private NoteService noteService;

    @BeforeEach
    void setUp() {
        noteService = new NoteService(noteRepository, noteTagRepository, noteMapper);
    }

    @Test
    void getNoteById_ShouldReturnNoteResponse() {
        UUID noteId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Note note = Note.builder().id(noteId).userId(userId).title("Test Note").build();
        NoteResponse response = NoteResponse.builder().id(noteId).title("Test Note").build();

        when(noteRepository.findByIdAndUserId(noteId, userId)).thenReturn(Optional.of(note));
        when(noteMapper.toResponse(note)).thenReturn(response);

        NoteResponse result = noteService.getNoteById(noteId, userId);
        assertNotNull(result);
        assertEquals("Test Note", result.getTitle());
    }
}
