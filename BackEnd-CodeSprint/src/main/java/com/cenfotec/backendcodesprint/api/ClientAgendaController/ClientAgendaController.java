package com.cenfotec.backendcodesprint.api.ClientAgendaController;

import com.cenfotec.backendcodesprint.logic.ClientAgenda.DTO.AgendaBookingResponseDTO;
import com.cenfotec.backendcodesprint.logic.ClientAgenda.DTO.RescheduleRequestDTO;
import com.cenfotec.backendcodesprint.logic.ClientAgenda.Service.ClientAgendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agenda-cliente")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ClientAgendaController {

    private final ClientAgendaService clientAgendaService;

//Obtener la agenda del cliente
    @GetMapping("/{clientProfileId}")
    public ResponseEntity<List<AgendaBookingResponseDTO>> getAgenda(
            @PathVariable Long clientProfileId) {
        return ResponseEntity.ok(clientAgendaService.getBookingsByClient(clientProfileId));
    }

    //Obtener el detalle completo de una cita específica
    @GetMapping("/{clientProfileId}/detail/{bookingId}")
    public ResponseEntity<AgendaBookingResponseDTO> getDetail(
            @PathVariable Long clientProfileId,
            @PathVariable Long bookingId) {
        return ResponseEntity.ok(clientAgendaService.getBookingDetail(bookingId, clientProfileId));
    }

//Cancelar una cita
    @PutMapping("/{clientProfileId}/cancel/{bookingId}")
    public ResponseEntity<AgendaBookingResponseDTO> cancel(
            @PathVariable Long clientProfileId,
            @PathVariable Long bookingId) {
        return ResponseEntity.ok(clientAgendaService.cancelBooking(bookingId, clientProfileId));
    }

//Reprogramar
    @PutMapping("/{clientProfileId}/reschedule/{bookingId}")
    public ResponseEntity<AgendaBookingResponseDTO> reschedule(
            @PathVariable Long clientProfileId,
            @PathVariable Long bookingId,
            @RequestBody RescheduleRequestDTO dto) {
        return ResponseEntity.ok(clientAgendaService.rescheduleBooking(bookingId, clientProfileId, dto));
    }
}