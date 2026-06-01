package com.productivityhub.planner.service;

import com.productivityhub.planner.dto.request.CreateEventRequest;
import com.productivityhub.planner.dto.request.UpdateEventRequest;
import com.productivityhub.planner.dto.response.EventResponse;
import com.productivityhub.planner.entity.Event;
import com.productivityhub.planner.exception.ResourceNotFoundException;
import com.productivityhub.planner.mapper.EventMapper;
import com.productivityhub.planner.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public List<EventResponse> getAllEvents(UUID userId, LocalDate start, LocalDate end) {
        if (start != null && end != null) {
            return eventRepository.findByUserIdAndEventDateBetweenOrderByEventDateAsc(userId, start, end)
                    .stream().map(eventMapper::toResponse).collect(Collectors.toList());
        }
        return eventRepository.findByUserIdOrderByEventDateAscStartTimeAsc(userId)
                .stream().map(eventMapper::toResponse).collect(Collectors.toList());
    }

    public List<EventResponse> getEventsByDate(UUID userId, LocalDate date) {
        return eventRepository.findByUserIdAndEventDate(userId, date)
                .stream().map(eventMapper::toResponse).collect(Collectors.toList());
    }

    public EventResponse getEventById(UUID id, UUID userId) {
        Event event = eventRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        return eventMapper.toResponse(event);
    }

    @Transactional
    public EventResponse createEvent(CreateEventRequest request, UUID userId) {
        Event event = Event.builder()
                .userId(userId)
                .title(request.getTitle())
                .description(request.getDescription())
                .eventDate(request.getEventDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .location(request.getLocation())
                .reminderMinutesBefore(request.getReminderMinutesBefore())
                .color(request.getColor())
                .build();

        event = eventRepository.save(event);
        log.info("Event created: {} for user {}", event.getId(), userId);
        return eventMapper.toResponse(event);
    }

    @Transactional
    public EventResponse updateEvent(UUID id, UpdateEventRequest request, UUID userId) {
        Event event = eventRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if (request.getTitle() != null) event.setTitle(request.getTitle());
        if (request.getDescription() != null) event.setDescription(request.getDescription());
        if (request.getEventDate() != null) event.setEventDate(request.getEventDate());
        if (request.getStartTime() != null) event.setStartTime(request.getStartTime());
        if (request.getEndTime() != null) event.setEndTime(request.getEndTime());
        if (request.getLocation() != null) event.setLocation(request.getLocation());
        if (request.getReminderMinutesBefore() != null) event.setReminderMinutesBefore(request.getReminderMinutesBefore());
        if (request.getColor() != null) event.setColor(request.getColor());

        event = eventRepository.save(event);
        log.info("Event updated: {}", event.getId());
        return eventMapper.toResponse(event);
    }

    @Transactional
    public void deleteEvent(UUID id, UUID userId) {
        Event event = eventRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        eventRepository.delete(event);
        log.info("Event deleted: {}", id);
    }

    public long getEventsCount(UUID userId, LocalDate date) {
        return eventRepository.countByUserIdAndEventDate(userId, date);
    }
}
