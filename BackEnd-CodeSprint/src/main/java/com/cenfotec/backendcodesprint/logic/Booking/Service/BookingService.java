package com.cenfotec.backendcodesprint.logic.Booking.Service;

import com.cenfotec.backendcodesprint.logic.Booking.DTO.*;
import com.cenfotec.backendcodesprint.logic.Booking.Repository.ServiceBookingRepository;
import com.cenfotec.backendcodesprint.logic.Model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final ServiceBookingRepository bookingRepo;

    // ── Listar todas las solicitudes de un proveedor ──────────────
    public List<BookingResponseDTO> getBookingsByProvider(Long providerProfileId) {
        return bookingRepo
                .findByCareService_ProviderProfile_IdOrderByCreatedDesc(providerProfileId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ── Listar filtradas por estado ───────────────────────────────
    public List<BookingResponseDTO> getBookingsByProviderAndStatus(
            Long providerProfileId, String status) {
        return bookingRepo
                .findByCareService_ProviderProfile_IdAndBookingStatusOrderByCreatedDesc(
                        providerProfileId, status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ── Detalle de una solicitud ──────────────────────────────────
    public BookingResponseDTO getBookingDetail(Long bookingId) {
        ServiceBooking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));
        return mapToResponse(booking);
    }

    // ── Aceptar o rechazar solicitud ──────────────────────────────
    @Transactional
    public BookingResponseDTO updateBookingStatus(Long bookingId,
                                                  BookingStatusUpdateDTO dto) {
        ServiceBooking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        if (!"pending".equals(booking.getBookingStatus())) {
            throw new RuntimeException("Only pending bookings can be accepted or rejected");
        }

        booking.setBookingStatus(dto.getStatus());

        if ("rejected".equals(dto.getStatus()) && dto.getRejectionReason() != null) {
            booking.setRejectionReason(dto.getRejectionReason());
        }

        return mapToResponse(bookingRepo.save(booking));
    }

    // ── Mapper ────────────────────────────────────────────────────
    private BookingResponseDTO mapToResponse(ServiceBooking b) {
        BookingResponseDTO d = new BookingResponseDTO();
        d.setId(b.getId());

        // Cliente
        ClientProfile cp = b.getClientProfile();
        d.setClientProfileId(cp.getId());
        d.setClientName(cp.getUser().getUserName() + " " + cp.getUser().getLastName());
        d.setClientPhone(cp.getPhone());

        // Senior
        SeniorProfile sp = b.getSeniorProfile();
        d.setSeniorProfileId(sp.getId());
        d.setSeniorName(sp.getUser().getUserName() + " " + sp.getUser().getLastName());

        // Servicio
        CareService cs = b.getCareService();
        d.setCareServiceId(cs.getId());
        d.setServiceName(cs.getTitle());
        d.setServiceCategory(cs.getServiceCategory() != null
                ? cs.getServiceCategory().getCategoryName() : null);

        // Reserva
        d.setScheduledAt(b.getScheduledAt());
        d.setAgreedPrice(b.getAgreedPrice());
        d.setAgreedPriceMode(b.getAgreedPriceMode());

        // Estado
        d.setBookingStatus(b.getBookingStatus());
        d.setRejectionReason(b.getRejectionReason());

        // Fecha de solicitud
        d.setRequestDate(b.getCreated());

        return d;
    }
}