package com.cenfotec.backendcodesprint.logic.TrakingSesion.Mapper;

import com.cenfotec.backendcodesprint.logic.Model.TrackingPoint;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Response.TrackingPointResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface TrackingPointMapper {
    @Mapping(target = "trackingPointId", source = "trackingPoint.id")
    @Mapping(target = "trackingSessionId", source = "trackingSession.id")
    TrackingPointResponseDto toResponse(TrackingPoint trackingPoint);
}
