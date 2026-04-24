package com.cenfotec.backendcodesprint.api.BookingController;

import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Request.BookingActionRequestDto;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Response.BookingActionResponseDto;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Response.ServiceBookingResponseDto;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Request.CreateServiceBookingRequestDto;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Service.ServiceBookingService;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Request.CancelBookingRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class ServiceBookingController {

    private final ServiceBookingService service;

    @PostMapping
    public ResponseEntity<ServiceBookingResponseDto> createBooking(
            @Valid @RequestBody CreateServiceBookingRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createBooking(dto));
    }

    @GetMapping("/provider/{providerProfileId}")
    public ResponseEntity<List<ServiceBookingResponseDto>> findByProvider(
            @PathVariable Long providerProfileId,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.findByProvider(providerProfileId, status));
    }

    @GetMapping("/client/{clientProfileId}/completed")
    public ResponseEntity<List<ServiceBookingResponseDto>> getCompletedByClient(
            @PathVariable Long clientProfileId) {
        return ResponseEntity.ok(service.findCompletedByClient(clientProfileId));
    }

    @GetMapping("/senior/{seniorProfileId}/completed")
    public ResponseEntity<List<ServiceBookingResponseDto>> getCompletedBySenior(
            @PathVariable Long seniorProfileId) {
        return ResponseEntity.ok(service.findCompletedBySenior(seniorProfileId));
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

    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingActionResponseDto> cancel(
            @PathVariable Long bookingId,
            @RequestParam Long providerProfileId,
            @Valid @RequestBody CancelBookingRequestDto dto) {
        return ResponseEntity.ok(service.cancelBooking(bookingId, providerProfileId, dto));
    }
}