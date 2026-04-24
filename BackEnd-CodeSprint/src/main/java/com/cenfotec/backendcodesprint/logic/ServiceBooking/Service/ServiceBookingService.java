package com.cenfotec.backendcodesprint.logic.ServiceBooking.Service;

import com.cenfotec.backendcodesprint.logic.Model.*;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.CareServiceRepository;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ClientProfileRepository;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.SeniorProfileRepository;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Request.BookingActionRequestDto;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Request.CancelBookingRequestDto;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Request.CreateServiceBookingRequestDto;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Response.BookingActionResponseDto;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Response.ServiceBookingResponseDto;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Mapper.ServiceBookingMapper;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Repository.ServiceBookingRepository;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Enums.AiStatus;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Repository.TelemedAiRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceBookingService {

    private static final Logger log = LoggerFactory.getLogger(ServiceBookingService.class);

    private final ServiceBookingMapper serviceBookingMapper;
    private final ServiceBookingRepository serviceBookingRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final SeniorProfileRepository seniorProfileRepository;
    private final CareServiceRepository careServiceRepository;
    private final TelemedAiRepository telemedSessionRepository;

    // ─── Helpers ──────────────────────────────────────────────────

    private boolean isTelemedicine(CareService careService) {
        String title    = careService.getTitle()    != null ? careService.getTitle().toLowerCase()    : "";
        String modality = careService.getModality() != null ? careService.getModality().toLowerCase() : "";
        String category = careService.getServiceCategory() != null &&
                careService.getServiceCategory().getCategoryName() != null
                ? careService.getServiceCategory().getCategoryName().toLowerCase() : "";
        return title.contains("telemedicina")
                || modality.contains("telemedicina")
                || category.contains("telemedicina");
    }

    // ─── Queries ──────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<ServiceBookingResponseDto> findByProvider(Long providerProfileId, String status) {
        List<ServiceBooking> bookings = (status == null || status.isBlank())
                ? serviceBookingRepository.findAllByCareService_ProviderProfile_Id(providerProfileId)
                : serviceBookingRepository.findByCareService_ProviderProfile_IdAndBookingStatus(
                providerProfileId, status.toUpperCase());
        return bookings.stream().map(serviceBookingMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ServiceBookingResponseDto> findCompletedByClient(Long clientProfileId) {
        return serviceBookingRepository
                .findByClientProfile_IdAndBookingStatus(clientProfileId, "COMPLETADA")
                .stream()
                .map(serviceBookingMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ServiceBookingResponseDto> findCompletedBySenior(Long seniorProfileId) {
        return serviceBookingRepository
                .findBySeniorProfile_IdAndBookingStatus(seniorProfileId, "COMPLETADA")
                .stream()
                .map(serviceBookingMapper::toResponse)
                .toList();
    }

    // ─── Crear reserva ────────────────────────────────────────────

    @Transactional
    public ServiceBookingResponseDto createBooking(CreateServiceBookingRequestDto dto) {

        // ── Resolver perfiles ──
        ClientProfile clientProfile = null;
        SeniorProfile seniorProfile = null;

        if (dto.getClientProfileId() != null) {
            clientProfile = clientProfileRepository.findById(dto.getClientProfileId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Cliente no encontrado."));
        } else {
            clientProfile = clientProfileRepository.findByUserId(dto.getUserId()).orElse(null);
        }

        if (dto.getSeniorProfileId() != null) {
            seniorProfile = seniorProfileRepository.findById(dto.getSeniorProfileId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Adulto mayor no encontrado."));
        } else {
            seniorProfile = seniorProfileRepository.findByUserId(dto.getUserId()).orElse(null);
        }

        if (clientProfile == null && seniorProfile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe un perfil de cliente o adulto mayor asociado al userId enviado.");
        }

        // ── Resolver servicio ──
        CareService careService = careServiceRepository.findById(dto.getCareServiceId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Servicio no encontrado."));

        // ── Determinar tipo ──
        boolean telemed = isTelemedicine(careService);
        String appointmentType = telemed ? "TELEMEDICINA" : "PRESENCIAL";

        // ── Crear booking ──
        ServiceBooking booking = new ServiceBooking();
        booking.setClientProfile(clientProfile);
        booking.setSeniorProfile(seniorProfile);
        booking.setCareService(careService);
        booking.setScheduledAt(dto.getScheduledAt());
        booking.setDestinationLatitude(dto.getDestinationLatitude());
        booking.setDestinationLongitude(dto.getDestinationLongitude());
        booking.setOriginLatitude(dto.getOriginLatitude());
        booking.setOriginLongitude(dto.getOriginLongitude());
        booking.setDestinationText(dto.getDestinationText());
        booking.setOriginText(dto.getOriginText());
        booking.setAgreedPrice(dto.getAgreedPrice());
        booking.setAgreedPriceMode(dto.getAgreedPriceMode());
        booking.setBookingStatus("PENDIENTE");
        booking.setAppointmentType(appointmentType);

        ServiceBooking savedBooking = serviceBookingRepository.save(booking);

        // ── Si es telemedicina → crear TelemedSession ──
        if (telemed) {
            try {
                TelemedSession telemedSession = new TelemedSession();
                telemedSession.setServiceBooking(savedBooking);
                telemedSession.setProviderProfile(careService.getProviderProfile());
                telemedSession.setClientProfile(clientProfile);
                telemedSession.setSeniorProfile(seniorProfile);
                telemedSession.setSessionState("scheduled");
                telemedSession.setStartedAt(OffsetDateTime.now());
                telemedSession.setAiConsent(false);
                telemedSession.setAiStatus(AiStatus.NOT_CONSENTED);

                TelemedSession savedSession = telemedSessionRepository.save(telemedSession);

                // ← LÍNEA CRÍTICA
                savedBooking.setTelemedSession(savedSession);

                log.info("[createBooking] TelemedSession id={} creada para booking={}",
                        savedSession.getId(), savedBooking.getId());

            } catch (Exception e) {
                log.error("[createBooking] Error creando TelemedSession: {}", e.getMessage(), e);
            }
        }

        // ── Log de diagnóstico ──
        log.info("[createBooking] telemedSession en booking antes del mapper = {}",
                savedBooking.getTelemedSession() != null
                        ? savedBooking.getTelemedSession().getId()
                        : "NULL");

        return serviceBookingMapper.toResponse(savedBooking);
    }

    // ─── Acciones del proveedor ───────────────────────────────────

    @Transactional
    public BookingActionResponseDto respond(Long bookingId, BookingActionRequestDto dto,
                                            Long providerProfileId) {
        ServiceBooking booking = getValidatedBooking(bookingId, providerProfileId);

        if (!"PENDIENTE".equals(booking.getBookingStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Solo se puede responder a una reserva PENDIENTE. Estado actual: "
                            + booking.getBookingStatus());
        }

        String nuevoEstado = "ACEPTAR".equals(dto.getAction()) ? "ACEPTADA" : "RECHAZADA";
        String mensaje = "ACEPTADA".equals(nuevoEstado)
                ? "Solicitud aceptada. El cliente ha sido notificado."
                : "Solicitud rechazada.";

        booking.setBookingStatus(nuevoEstado);
        serviceBookingRepository.save(booking);

        return new BookingActionResponseDto(booking.getId(), nuevoEstado, mensaje);
    }

    @Transactional
    public BookingActionResponseDto startService(Long bookingId, Long providerProfileId) {
        ServiceBooking booking = getValidatedBooking(bookingId, providerProfileId);

        if (!"ACEPTADA".equals(booking.getBookingStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Solo se puede iniciar un servicio ACEPTADO. Estado actual: "
                            + booking.getBookingStatus());
        }

        booking.setBookingStatus("EN_CURSO");
        serviceBookingRepository.save(booking);

        return new BookingActionResponseDto(booking.getId(), "EN_CURSO",
                "Servicio iniciado. Ya puedes activar el tracking.");
    }

    @Transactional
    public BookingActionResponseDto completeService(Long bookingId, Long providerProfileId) {
        ServiceBooking booking = getValidatedBooking(bookingId, providerProfileId);

        if (!"EN_CURSO".equals(booking.getBookingStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Solo se puede completar un servicio EN_CURSO. Estado actual: "
                            + booking.getBookingStatus());
        }

        booking.setBookingStatus("COMPLETADA");
        serviceBookingRepository.save(booking);

        return new BookingActionResponseDto(booking.getId(), "COMPLETADA",
                "Servicio completado exitosamente.");
    }

    @Transactional
    public BookingActionResponseDto cancelBooking(Long bookingId, Long providerProfileId,
                                                  CancelBookingRequestDto dto) {
        ServiceBooking booking = getValidatedBooking(bookingId, providerProfileId);

        if ("COMPLETADA".equals(booking.getBookingStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede cancelar una reserva COMPLETADA.");
        }
        if ("RECHAZADA".equals(booking.getBookingStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede cancelar una reserva RECHAZADA.");
        }

        booking.setBookingStatus("CANCELADA");
        serviceBookingRepository.save(booking);

        return new BookingActionResponseDto(booking.getId(), "CANCELADA",
                "Reserva cancelada correctamente.");
    }

    // ─── Validación interna ───────────────────────────────────────

    public ServiceBooking getValidatedBooking(Long bookingId, Long providerProfileId) {
        ServiceBooking booking = serviceBookingRepository.findByIdWithProvider(bookingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Reserva no encontrada."));

        Long assigned = booking.getCareService().getProviderProfile().getId();
        if (!assigned.equals(providerProfileId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado.");
        }
        return booking;
    }
}