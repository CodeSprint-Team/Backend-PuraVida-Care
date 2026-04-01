package com.cenfotec.backendcodesprint.logic.CreateService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceStatsDTO {
    private long total;
    private long active;
    private long paused;
}