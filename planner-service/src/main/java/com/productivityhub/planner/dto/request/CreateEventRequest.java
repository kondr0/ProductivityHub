package com.productivityhub.planner.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CreateEventRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 256)
    private String title;

    private String description;

    @NotNull(message = "Event date is required")
    private LocalDate eventDate;

    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private Integer reminderMinutesBefore;
    private String color;
}
