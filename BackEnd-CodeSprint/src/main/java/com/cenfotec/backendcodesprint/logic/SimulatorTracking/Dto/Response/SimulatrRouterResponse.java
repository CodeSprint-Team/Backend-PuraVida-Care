package com.cenfotec.backendcodesprint.logic.SimulatorTracking.Dto.Response;

import com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Response.TrackingPointResponseDto;
import lombok.*;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimulatrRouterResponse {
    private Long sessionId;
    private int totalPointsGenerated;
    private int totalSegments;
    private String status;
    private List<TrackingPointResponseDto> points;
}
