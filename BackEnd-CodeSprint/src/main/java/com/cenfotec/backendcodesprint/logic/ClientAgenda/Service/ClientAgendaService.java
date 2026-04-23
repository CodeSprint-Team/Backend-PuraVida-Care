package com.cenfotec.backendcodesprint.logic.ClientAgenda.Service;

import com.cenfotec.backendcodesprint.logic.ClientAgenda.DTO.AgendaBookingResponseDTO;
import com.cenfotec.backendcodesprint.logic.ClientAgenda.DTO.RescheduleRequestDTO;
import com.cenfotec.backendcodesprint.logic.ClientAgenda.Mapper.ClientAgendaMapper;
import com.cenfotec.backendcodesprint.logic.ClientAgenda.Repository.ClientAgendaRepository;
import com.cenfotec.backendcodesprint.logic.Model.ServiceBooking;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Request.CancelBookingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientAgendaService {

    private final ClientAgendaRepository clientAgendaRepository;
    private final ClientAgendaMapper     clientAgendaMapper;

    // Lista de citas del cliente
    public List<AgendaBookingResponseDTO> getBookingsByClient(Long clientProfileId) {
        return clientAgendaRepository
                .findByClientProfile_IdOrderByScheduledAtDesc(clientProfileId)
                .stream()
                .map(clientAgendaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Detalle de una cita
    public AgendaBookingResponseDTO getBookingDetail(Long bookingId, Long clientProfileId) {
        ServiceBooking booking = clientAgendaRepository
                .findByIdAndClientProfile_Id(bookingId, clientProfileId)
                .orElseThrow(() -> new RuntimeException(
                        "Cita no encontrada o no pertenece al cliente: " + bookingId));
        return clientAgendaMapper.toDTO(booking);
    }

    // Cancelar
    @Transactional
    public AgendaBookingResponseDTO cancelBooking(Long bookingId, Long clientProfileId, CancelBookingRequestDto dto) {
        ServiceBooking booking = clientAgendaRepository
                .findByIdAndClientProfile_Id(bookingId, clientProfileId)
                .orElseThrow(() -> new RuntimeException(
                        "Cita no encontrada o no pertenece al cliente: " + bookingId));

        if ("CANCELADO".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new RuntimeException("La cita ya está cancelada");
        }
        if ("COMPLETADO".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new RuntimeException("No se puede cancelar una cita completada");
        }

        booking.setBookingStatus("CANCELADO");
        booking.setCancellationReason(dto.getReason());
        return clientAgendaMapper.toDTO(clientAgendaRepository.save(booking));
    }

    // Reprogramar
    @Transactional
    public AgendaBookingResponseDTO rescheduleBooking(Long bookingId, Long clientProfileId,
                                                      RescheduleRequestDTO dto) {
        ServiceBooking booking = clientAgendaRepository
                .findByIdAndClientProfile_Id(bookingId, clientProfileId)
                .orElseThrow(() -> new RuntimeException(
                        "Cita no encontrada o no pertenece al cliente: " + bookingId));

        if ("CANCELADO".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new RuntimeException("No se puede reprogramar una cita cancelada");
        }
        if ("COMPLETADO".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new RuntimeException("No se puede reprogramar una cita completada");
        }

        // Guardar la fecha anterior antes de sobrescribirla
        booking.setPreviousScheduledAt(booking.getScheduledAt());

        // Nueva fecha
        booking.setScheduledAt(dto.getScheduledAt());

        // Motivo de reagendación
        booking.setRescheduleReason(dto.getRescheduleReason());

        // Vuelve a pendiente
        booking.setBookingStatus("PENDIENTE");

        return clientAgendaMapper.toDTO(clientAgendaRepository.save(booking));
    }
}