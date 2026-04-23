package com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelBookingRequestDto {

    @JsonAlias({"cancellationReason"})
    @NotBlank(message = "El motivo de cancelación es obligatorio.")
    private String reason;
}