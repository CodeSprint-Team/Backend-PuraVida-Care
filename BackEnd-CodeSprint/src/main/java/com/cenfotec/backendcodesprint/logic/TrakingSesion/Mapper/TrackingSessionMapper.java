package com.cenfotec.backendcodesprint.logic.TrakingSesion.Mapper;

import com.cenfotec.backendcodesprint.logic.Model.TrackingSession;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Response.TrackingSessionResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrackingSessionMapper {

    @Mapping(target = "trackingSessionId", source = "id")
    @Mapping(target = "bookingId", source = "serviceBooking.id")
    @Mapping(target = "providerProfileId", source = "providerProfile.id")
    TrackingSessionResponseDto toResponse(TrackingSession trackingSession);
}
