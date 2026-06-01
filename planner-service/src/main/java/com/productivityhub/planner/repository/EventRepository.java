package com.productivityhub.planner.repository;

import com.productivityhub.planner.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    List<Event> findByUserIdOrderByEventDateAscStartTimeAsc(UUID userId);

    List<Event> findByUserIdAndEventDateBetweenOrderByEventDateAsc(
            UUID userId, LocalDate start, LocalDate end);

    List<Event> findByUserIdAndEventDate(UUID userId, LocalDate date);

    Optional<Event> findByIdAndUserId(UUID id, UUID userId);

    void deleteByIdAndUserId(UUID id, UUID userId);

    long countByUserIdAndEventDate(UUID userId, LocalDate date);
}
