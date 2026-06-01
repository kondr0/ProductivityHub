package com.productivityhub.notes.repository;

import com.productivityhub.notes.entity.NoteTag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteTagRepository extends JpaRepository<NoteTag, UUID> {
    List<NoteTag> findByUserId(UUID userId);
    Optional<NoteTag> findByNameAndUserId(String name, UUID userId);
}
