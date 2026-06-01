package com.productivityhub.dashboard.dto;

import lombok.*;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DashboardResponse {
    private UUID userId;
    private List<WidgetData> widgets;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class WidgetData {
        private String id;
        private String type;
        private String title;
        private Object data;
    }
}
