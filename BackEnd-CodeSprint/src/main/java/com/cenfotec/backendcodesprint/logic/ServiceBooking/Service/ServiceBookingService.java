package com.cenfotec.backendcodesprint.logic.ServiceBooking.Service;


import com.cenfotec.backendcodesprint.logic.Model.ServiceBooking;
import com.cenfotec.backendcodesprint.logic.Model.ClientProfile;
import com.cenfotec.backendcodesprint.logic.Model.SeniorProfile;
import com.cenfotec.backendcodesprint.logic.Model.CareService;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.CareServiceRepository;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ClientProfileRepository;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.SeniorProfileRepository;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Request.BookingActionRequestDto;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Response.BookingActionResponseDto;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Response.ServiceBookingResponseDto;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Request.CreateServiceBookingRequestDto;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Mapper.ServiceBookingMapper;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Repository.ServiceBookingRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceBookingService {

    private final ServiceBookingMapper serviceBookingMapper;
    private final ServiceBookingRepository serviceBookingRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final SeniorProfileRepository seniorProfileRepository;
    private final CareServiceRepository careServiceRepository;



    @Transactional(readOnly = true)
        public List<ServiceBookingResponseDto> findByProvider(Long providerProfileId, String status){
        List<ServiceBooking> bookings= (status == null || status.isBlank())
                ? serviceBookingRepository.findAllByCareService_ProviderProfile_Id(providerProfileId)
                : serviceBookingRepository.findByCareService_ProviderProfile_IdAndBookingStatus(providerProfileId, status.toUpperCase());

        return bookings.stream().map(serviceBookingMapper ::toResponse ).toList();
          }

    @Transactional
    public BookingActionResponseDto respond(Long bookingId, BookingActionRequestDto bookingActionRequestDto,
                                            Long providerProfileId) {
        ServiceBooking booking = getValidatedBooking(bookingId, providerProfileId);

        if (!"PENDIENTE".equals(booking.getBookingStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Solo se puede responder a una reserva PENDIENTE. Estado actual: "
                            + booking.getBookingStatus());
        }

        String nuevoEstado = "ACEPTAR".equals(bookingActionRequestDto.getAction()) ? "ACEPTADA" : "RECHAZADA";
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



    public ServiceBooking getValidatedBooking(Long bookingId, Long providerProfileId){
        ServiceBooking serviceBooking = serviceBookingRepository.findByIdWithProvider(bookingId) .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Reserva no encontrada."));

        Long assigned = serviceBooking.getCareService().getProviderProfile().getId();

        if (!assigned.equals(providerProfileId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado.");
        }
        return serviceBooking;
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


    @Transactional
    public ServiceBookingResponseDto createBooking(CreateServiceBookingRequestDto dto) {

        ClientProfile clientProfile = clientProfileRepository.findById(dto.getClientProfileId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado."
                ));

        SeniorProfile seniorProfile = seniorProfileRepository.findById(dto.getSeniorProfileId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Adulto mayor no encontrado."
                ));

        CareService careService = careServiceRepository.findById(dto.getCareServiceId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Servicio no encontrado."
                ));

        ServiceBooking booking = new ServiceBooking();
        booking.setClientProfile(clientProfile);
        booking.setSeniorProfile(seniorProfile);
        booking.setCareService(careService);
        booking.setScheduledAt(dto.getScheduledAt());
        booking.setDestinationLatitude(dto.getDestinationLatitude());
        booking.setDestinationLongitude(dto.getDestinationLongitude());
        booking.setOriginLatitude(dto.getOriginLatitude());
        booking.setOriginLongitude(dto.getOriginLongitude());
        booking.setAgreedPrice(dto.getAgreedPrice());
        booking.setAgreedPriceMode(dto.getAgreedPriceMode());
        booking.setBookingStatus("PENDIENTE");

        ServiceBooking savedBooking = serviceBookingRepository.save(booking);

        return serviceBookingMapper.toResponse(savedBooking);
    }

}
