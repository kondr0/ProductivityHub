package com.productivityhub.notes.repository;

import com.productivityhub.notes.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, UUID> {

    List<Note> findByUserIdOrderByCreatedAtDesc(UUID userId);

    Optional<Note> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT n FROM Note n WHERE n.userId = :userId AND " +
           "(LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(n.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Note> search(@Param("userId") UUID userId, @Param("query") String query);

    List<Note> findByUserIdAndIsPinnedTrue(UUID userId);

    void deleteByIdAndUserId(UUID id, UUID userId);

    long countByUserId(UUID userId);
}
