package com.cenfotec.backendcodesprint.api.BookingController;

import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Request.BookingActionRequestDto;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Response.BookingActionResponseDto;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Response.ServiceBookingResponseDto;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Service.ServiceBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class ServiceBookingController {

    private final ServiceBookingService service;

    @GetMapping("/provider/{providerProfileId}")
    public ResponseEntity<List<ServiceBookingResponseDto>> findByProvider(
            @PathVariable Long providerProfileId,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.findByProvider(providerProfileId, status));
    }

    @PatchMapping("/{bookingId}/respond")
    public ResponseEntity<BookingActionResponseDto> respond(
            @PathVariable Long bookingId,
            @RequestParam Long providerProfileId,
            @Valid @RequestBody BookingActionRequestDto dto) {
        return ResponseEntity.ok(service.respond(bookingId, dto, providerProfileId));
    }

    @PatchMapping("/{bookingId}/complete")
    public ResponseEntity<BookingActionResponseDto> complete(
            @PathVariable Long bookingId,
            @RequestParam Long providerProfileId) {
        return ResponseEntity.ok(service.completeService(bookingId, providerProfileId));
    }

    @PatchMapping("/{bookingId}/start-service")
    public ResponseEntity<BookingActionResponseDto> startService(
            @PathVariable Long bookingId,
            @RequestParam Long providerProfileId) {
        return ResponseEntity.ok(service.startService(bookingId, providerProfileId));
    }
}