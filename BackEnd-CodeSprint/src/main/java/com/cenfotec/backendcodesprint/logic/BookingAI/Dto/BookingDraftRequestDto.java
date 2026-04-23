package com.cenfotec.backendcodesprint.logic.BookingAI.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class BookingDraftRequestDto {

    @NotBlank
    private String category;

    @NotBlank
    private String scheduledAt;

    @NotBlank
    private String originAddress;

    @NotBlank
    private String destinationAddress;

    private BigDecimal originLatitude;
    private BigDecimal originLongitude;
    private BigDecimal destinationLatitude;
    private BigDecimal destinationLongitude;

    private String mobility;
    private String companion;
    private String zone;

    private Long careServiceId;
    private Long seniorProfileId;
    private String notes;
}
