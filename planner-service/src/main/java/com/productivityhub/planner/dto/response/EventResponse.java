package com.productivityhub.planner.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EventResponse {
    private UUID id;
    private UUID userId;
    private String title;
    private String description;
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private Integer reminderMinutesBefore;
    private String color;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
