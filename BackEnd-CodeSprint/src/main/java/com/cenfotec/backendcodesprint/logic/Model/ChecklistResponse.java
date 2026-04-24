package com.cenfotec.backendcodesprint.logic.Model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChecklistResponse {
    private Long id;
    private Long homeId;
    private Long serviceBookingId;
    private Long completedBy;
    private LocalDateTime completedAt;
    private Boolean isCompleted;
}