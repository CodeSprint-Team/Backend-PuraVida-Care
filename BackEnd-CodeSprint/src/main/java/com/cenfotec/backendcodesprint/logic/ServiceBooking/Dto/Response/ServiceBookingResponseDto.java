package com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceBookingResponseDto {
    private Long bookingId;
    private String bookingStatus;
    private LocalDateTime scheduledAt;

    private Long clientProfileId;
    private String clientName;

    private Long seniorProfileId;
    private String seniorName;

    private Long careServiceId;
    private String serviceTitle;
    private Long providerProfileId;
    private String providerName;
    private String providerProfileImage;

    private BigDecimal agreedPrice;
    private String agreedPriceMode;

    private BigDecimal originLatitude;
    private BigDecimal originLongitude;
    private BigDecimal destinationLatitude;
    private BigDecimal destinationLongitude;
}
