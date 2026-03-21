package com.cenfotec.backendcodesprint.api.BookingController;

import com.cenfotec.backendcodesprint.logic.Booking.DTO.BookingResponseDTO;
import com.cenfotec.backendcodesprint.logic.Booking.DTO.BookingStatusUpdateDTO;
import com.cenfotec.backendcodesprint.logic.Booking.Service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/provider/{providerProfileID}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByProvider(
            @PathVariable Long providerProfileID,
            @RequestParam(required = false) String status
    ){
        if (status != null && !status.isBlank()) {
            return ResponseEntity.ok(bookingService.getBookingsByProviderAndStatus(providerProfileID, status));
        }
        return ResponseEntity.ok(bookingService.getBookingsByProvider(providerProfileID));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDTO> getBookingDetail(
            @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.getBookingDetail(bookingId));
    }

    @PatchMapping("/{bookingId}/status")
    public ResponseEntity<BookingResponseDTO> updateStatus(
            @PathVariable Long bookingId,
            @Valid @RequestBody BookingStatusUpdateDTO dto) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(bookingId, dto));
    }
}
