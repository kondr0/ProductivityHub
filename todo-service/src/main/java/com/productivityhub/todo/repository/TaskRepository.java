package com.productivityhub.todo.repository;

import com.productivityhub.todo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByUserIdOrderByCreatedAtDesc(UUID userId);

    Optional<Task> findByIdAndUserId(UUID id, UUID userId);

    List<Task> findByUserIdAndStatus(UUID userId, Task.TaskStatus status);

    List<Task> findByUserIdAndPriority(UUID userId, Task.Priority priority);

    @Query("SELECT t FROM Task t JOIN t.tags tg WHERE t.userId = :userId AND tg.id = :tagId")
    List<Task> findByUserIdAndTagId(@Param("userId") UUID userId, @Param("tagId") UUID tagId);

    List<Task> findByUserIdAndDueDate(UUID userId, LocalDate dueDate);

    List<Task> findByUserIdAndDueDateBefore(UUID userId, LocalDate dueDate);

    void deleteByIdAndUserId(UUID id, UUID userId);
}
