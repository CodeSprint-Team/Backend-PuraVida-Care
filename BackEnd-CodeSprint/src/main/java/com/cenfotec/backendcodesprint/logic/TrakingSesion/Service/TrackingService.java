package com.cenfotec.backendcodesprint.logic.TrakingSesion.Service;

import com.cenfotec.backendcodesprint.logic.Model.ProviderProfile;
import com.cenfotec.backendcodesprint.logic.Model.ServiceBooking;
import com.cenfotec.backendcodesprint.logic.Model.TrackingPoint;
import com.cenfotec.backendcodesprint.logic.Model.TrackingSession;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ProviderProfileRepository;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Service.ServiceBookingService;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Request.TrackingPointRequestDto;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Request.TrackingSessionRequestDto;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Response.TrackingPointResponseDto;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Response.TrackingSessionResponseDto;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Mapper.TrackingPointMapper;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Mapper.TrackingSessionMapper;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Repository.TrackingPonitRepository;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Repository.TrackingSessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final TrackingPonitRepository trackingPonitRepository;
    private final TrackingSessionRepository trackingSessionRepository;
    private final TrackingPointMapper trackingPointMapper;
    private final TrackingSessionMapper trackingSessionMapper;
    private final ServiceBookingService serviceBookingService;
    private final ProviderProfileRepository providerProfileRepository;

    @Transactional
    public TrackingSessionResponseDto startTrackingSession(TrackingSessionRequestDto trackingSessionRequestDto,Long providerProfileId) {

        ServiceBooking booking = serviceBookingService.getValidatedBooking(trackingSessionRequestDto.getBookingId(),providerProfileId);
        if(!"EN_CURSO".equals(booking.getBookingStatus())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Tracking disponible solo en servicio en curso. Estado actual: "
                            + booking.getBookingStatus());
        }
        trackingSessionRepository.findByServiceBookingIdAndTrackingState(
                trackingSessionRequestDto.getBookingId(), "active"
        ).ifPresent(ts -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una sesión de tracking activa para esta reserva.");
        });

        ProviderProfile provider = providerProfileRepository.findById(providerProfileId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));

        TrackingSession session = new TrackingSession();
        session.setProviderProfile(provider);
        session.setServiceBooking(booking);
        session.setTrackingState("active");
        session.setStartedAt(OffsetDateTime.now());
        trackingSessionRepository.save(session);
        return trackingSessionMapper.toResponse(session);

    }

    @Transactional
    public TrackingSessionResponseDto stopTrackingSession(Long sessionId, Long providerProfileId) {
        TrackingSession session = getValidationSession(sessionId, providerProfileId);
        if(!"active".equals(session.getTrackingState())){
            throw new ResponseStatusException(HttpStatus.CONFLICT
            , "La session de traking ya no esta activa ");
        }
        session.setTrackingState("ended");
        session.setEndedAt(OffsetDateTime.now());

        ServiceBooking booking = session.getServiceBooking();
        booking.setBookingStatus("COMPLETADA");
        trackingSessionRepository.save(session);
        return trackingSessionMapper.toResponse(session);
    }

    @Transactional
    public TrackingPointResponseDto addPoint(Long sessionId,
                                             TrackingPointRequestDto dto,
                                             Long providerProfileId) {
        TrackingSession session = getValidationSession(sessionId, providerProfileId);

        if (!"active".equals(session.getTrackingState())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se pueden agregar puntos a una sesión que no está activa.");
        }

        TrackingPoint point = new TrackingPoint();
        point.setTrackingSession(session);
        point.setLatitude(BigDecimal.valueOf(dto.getLatitude()));
        point.setLongitude(BigDecimal.valueOf(dto.getLongitude()));
        point.setRecordedAt(System.currentTimeMillis());
        trackingPonitRepository.save(point);

        return trackingPointMapper.toResponse(point);
    }



    private TrackingSession getValidationSession(Long sessionId, Long providerProfileId) {
        TrackingSession session = trackingSessionRepository.findById(sessionId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session no encontrado"));

        if (!session.getProviderProfile().getId().equals(providerProfileId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado.");
        }
        return session;
    }
}
