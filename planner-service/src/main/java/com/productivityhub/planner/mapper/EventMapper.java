package com.productivityhub.planner.mapper;

import com.productivityhub.planner.dto.response.EventResponse;
import com.productivityhub.planner.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventResponse toResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .userId(event.getUserId())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .location(event.getLocation())
                .reminderMinutesBefore(event.getReminderMinutesBefore())
                .color(event.getColor())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}
