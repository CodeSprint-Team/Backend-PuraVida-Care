package com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateServiceBookingRequestDto {

    @NotNull(message = "El userId es obligatorio.")
    private Long userId;

    private Long clientProfileId;

    private Long seniorProfileId;

    @NotNull(message = "El careServiceId es obligatorio.")
    private Long careServiceId;

    @NotNull(message = "La fecha y hora programada son obligatorias.")
    private LocalDateTime scheduledAt;

    private BigDecimal destinationLatitude;

    private BigDecimal destinationLongitude;

    private BigDecimal originLatitude;

    private BigDecimal originLongitude;

    private String originText;

    private String destinationText;

    @NotNull(message = "El precio acordado es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio acordado debe ser mayor a 0.")
    private BigDecimal agreedPrice;

    @NotBlank(message = "El modo de precio acordado es obligatorio.")
    private String agreedPriceMode;
}