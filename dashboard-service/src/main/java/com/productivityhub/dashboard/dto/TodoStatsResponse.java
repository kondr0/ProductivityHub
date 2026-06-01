package com.productivityhub.dashboard.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TodoStatsResponse {
    private long total;
    private long todo;
    private long inProgress;
    private long done;
    private long overdue;
}
