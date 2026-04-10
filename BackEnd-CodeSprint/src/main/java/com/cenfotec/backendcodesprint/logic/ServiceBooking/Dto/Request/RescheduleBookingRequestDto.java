package com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RescheduleBookingRequestDto {

    @NotNull(message = "La nueva fecha y hora son obligatorias.")
    private LocalDateTime newScheduledAt;

    @NotBlank(message = "El motivo de reprogramación es obligatorio.")
    private String reason;
}