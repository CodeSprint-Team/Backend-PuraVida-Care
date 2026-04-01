package com.cenfotec.backendcodesprint.logic.SimulatorTracking.Mapper;

import com.cenfotec.backendcodesprint.logic.SimulatorTracking.Dto.Response.SimulatrRouterResponse;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Response.TrackingPointResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SimulationMapper {

    default SimulatrRouterResponse toResponse(
            Long sessionId,
            List<TrackingPointResponseDto> points,
            int totalSegments) {

        return SimulatrRouterResponse.builder()
                .sessionId(sessionId)
                .totalPointsGenerated(points.size())
                .totalSegments(totalSegments)
                .status("COMPLETED")
                .points(points)
                .build();
    }
}