package com.productivityhub.planner.service;

import com.productivityhub.planner.dto.response.EventResponse;
import com.productivityhub.planner.entity.Event;
import com.productivityhub.planner.mapper.EventMapper;
import com.productivityhub.planner.repository.EventRepository;
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
class EventServiceTest {

    @Mock private EventRepository eventRepository;
    @Mock private EventMapper eventMapper;

    private EventService eventService;

    @BeforeEach
    void setUp() {
        eventService = new EventService(eventRepository, eventMapper);
    }

    @Test
    void getEventById_ShouldReturnEventResponse() {
        UUID eventId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Event event = Event.builder().id(eventId).userId(userId).title("Test Event").build();
        EventResponse response = EventResponse.builder().id(eventId).title("Test Event").build();

        when(eventRepository.findByIdAndUserId(eventId, userId)).thenReturn(Optional.of(event));
        when(eventMapper.toResponse(event)).thenReturn(response);

        EventResponse result = eventService.getEventById(eventId, userId);
        assertNotNull(result);
        assertEquals("Test Event", result.getTitle());
    }
}
