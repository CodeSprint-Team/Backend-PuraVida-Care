package com.cenfotec.backendcodesprint.logic.TrackingService;

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
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Service.TrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrackingServiceTest {

    @Mock
    private TrackingPonitRepository trackingPonitRepository;

    @Mock
    private TrackingSessionRepository trackingSessionRepository;

    @Mock
    private TrackingPointMapper trackingPointMapper;

    @Mock
    private TrackingSessionMapper trackingSessionMapper;

    @Mock
    private ServiceBookingService serviceBookingService;

    @Mock
    private ProviderProfileRepository providerProfileRepository;

    @InjectMocks
    private TrackingService trackingService;

    private ServiceBooking booking;
    private ProviderProfile provider;
    private TrackingSession session;

    @BeforeEach
    void setUp() {
        booking = new ServiceBooking();
        booking.setId(1L);
        booking.setBookingStatus("EN_CURSO");

        provider = new ProviderProfile();
        provider.setId(10L);

        session = new TrackingSession();
        session.setId(100L);
        session.setProviderProfile(provider);
        session.setServiceBooking(booking);
        session.setTrackingState("active");
    }

    @Test
    void startTrackingSession_ShouldCreateSessionSuccessfully() {

        TrackingSessionRequestDto dto = new TrackingSessionRequestDto();
        dto.setBookingId(1L);

        when(serviceBookingService.getValidatedBooking(1L, 10L)).thenReturn(booking);
        when(trackingSessionRepository.findByServiceBookingIdAndTrackingState(1L, "active"))
                .thenReturn(Optional.empty());
        when(providerProfileRepository.findById(10L)).thenReturn(Optional.of(provider));
        when(trackingSessionMapper.toResponse(any(TrackingSession.class)))
                .thenReturn(new TrackingSessionResponseDto());

        TrackingSessionResponseDto result = trackingService.startTrackingSession(dto, 10L);

        assertNotNull(result);
        verify(trackingSessionRepository, times(1)).save(any(TrackingSession.class));
    }

    @Test
    void startTrackingSession_WhenBookingIsNotInProgress_ShouldThrowException() {

        TrackingSessionRequestDto dto = new TrackingSessionRequestDto();
        dto.setBookingId(1L);

        booking.setBookingStatus("PENDIENTE");

        when(serviceBookingService.getValidatedBooking(1L, 10L)).thenReturn(booking);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            trackingService.startTrackingSession(dto, 10L);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Tracking disponible solo en servicio en curso"));
    }

    @Test
    void startTrackingSession_WhenSessionAlreadyExists_ShouldThrowException() {

        TrackingSessionRequestDto dto = new TrackingSessionRequestDto();
        dto.setBookingId(1L);

        when(serviceBookingService.getValidatedBooking(1L, 10L)).thenReturn(booking);
        when(trackingSessionRepository.findByServiceBookingIdAndTrackingState(1L, "active"))
                .thenReturn(Optional.of(session));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            trackingService.startTrackingSession(dto, 10L);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Ya existe una sesión de tracking activa"));
    }

    @Test
    void stopTrackingSession_ShouldEndSessionSuccessfully() {

        when(trackingSessionRepository.findById(100L)).thenReturn(Optional.of(session));
        when(trackingSessionMapper.toResponse(session)).thenReturn(new TrackingSessionResponseDto());

        TrackingSessionResponseDto result = trackingService.stopTrackingSession(100L, 10L);

        assertNotNull(result);
        assertEquals("ended", session.getTrackingState());
        assertEquals("COMPLETADA", booking.getBookingStatus());

        verify(trackingSessionRepository, times(1)).save(session);
    }

    @Test
    void stopTrackingSession_WhenSessionIsNotActive_ShouldThrowException() {

        session.setTrackingState("ended");

        when(trackingSessionRepository.findById(100L)).thenReturn(Optional.of(session));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            trackingService.stopTrackingSession(100L, 10L);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getReason().contains("La session de traking ya no esta activa"));
    }

    @Test
    void addPoint_ShouldSavePointSuccessfully() {

        TrackingPointRequestDto dto = new TrackingPointRequestDto();
        dto.setLatitude(9.934);
        dto.setLongitude(-84.087);

        when(trackingSessionRepository.findById(100L)).thenReturn(Optional.of(session));
        when(trackingPointMapper.toResponse(any(TrackingPoint.class)))
                .thenReturn(new TrackingPointResponseDto());

        TrackingPointResponseDto result = trackingService.addPoint(100L, dto, 10L);

        assertNotNull(result);
        verify(trackingPonitRepository, times(1)).save(any(TrackingPoint.class));
    }

    @Test
    void addPoint_WhenSessionIsNotActive_ShouldThrowException() {

        TrackingPointRequestDto dto = new TrackingPointRequestDto();
        dto.setLatitude(9.934);
        dto.setLongitude(-84.087);

        session.setTrackingState("ended");

        when(trackingSessionRepository.findById(100L)).thenReturn(Optional.of(session));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            trackingService.addPoint(100L, dto, 10L);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getReason().contains("No se pueden agregar puntos"));
    }

    @Test
    void getPointsBySession_ShouldReturnPointList() {

        TrackingPoint point = new TrackingPoint();

        when(trackingSessionRepository.findById(100L)).thenReturn(Optional.of(session));
        when(trackingPonitRepository.findByTrackingSessionIdOrderByRecordedAtAsc(100L))
                .thenReturn(List.of(point));
        when(trackingPointMapper.toResponse(point)).thenReturn(new TrackingPointResponseDto());

        List<TrackingPointResponseDto> result = trackingService.getPointsBySession(100L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getSession_ShouldReturnSessionSuccessfully() {

        when(trackingSessionRepository.findById(100L)).thenReturn(Optional.of(session));
        when(trackingSessionMapper.toResponse(session)).thenReturn(new TrackingSessionResponseDto());

        TrackingSessionResponseDto result = trackingService.getSession(100L);

        assertNotNull(result);
    }

    @Test
    void getValidationSession_WhenProviderDoesNotMatch_ShouldThrowException() {

        ProviderProfile anotherProvider = new ProviderProfile();
        anotherProvider.setId(999L);
        session.setProviderProfile(anotherProvider);

        when(trackingSessionRepository.findById(100L)).thenReturn(Optional.of(session));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            trackingService.stopTrackingSession(100L, 10L);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Acceso denegado"));
    }
}


