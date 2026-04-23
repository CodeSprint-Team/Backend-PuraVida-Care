package com.cenfotec.backendcodesprint.logic.ClientAgenda.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RescheduleRequestDTO {
    private LocalDateTime scheduledAt;

    private String rescheduleReason;
}