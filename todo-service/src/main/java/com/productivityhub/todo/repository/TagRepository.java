package com.productivityhub.todo.repository;

import com.productivityhub.todo.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {
    List<Tag> findByUserId(UUID userId);
    Optional<Tag> findByNameAndUserId(String name, UUID userId);
}
