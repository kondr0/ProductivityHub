package com.productivityhub.planner.dto.request;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UpdateEventRequest {
    private String title;
    private String description;
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private Integer reminderMinutesBefore;
    private String color;
}
