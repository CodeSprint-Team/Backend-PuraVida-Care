package com.cenfotec.backendcodesprint.logic.ClientAgenda.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AgendaBookingResponseDTO {

    // ── Booking
    private Long            bookingId;
    private String          bookingStatus;
    private LocalDateTime   scheduledAt;
    private BigDecimal      agreedPrice;
    private String          agreedPriceMode;
    private BigDecimal      originLatitude;
    private BigDecimal      originLongitude;
    private BigDecimal      destinationLatitude;
    private BigDecimal      destinationLongitude;
    private String          rejectionReason;
    private LocalDateTime previousScheduledAt;
    private String rescheduleReason;
    private String          cancellationReason;
    private LocalDateTime   createdAt;

    // ── Servicio
    private String          serviceTitle;
    private String          serviceDescription;
    private String          categoryName;

    // ── Proveedor
    private Long            providerProfileId;
    private String          providerFullName;
    private String          providerTypeName;
    private String          providerPhone;
    private String          providerZone;
    private String          providerProfileImage;

    // ── Adulto mayor
    private Long            seniorProfileId;
    private String          seniorFullName;
    private String          seniorAddress;

    // ── Cliente (quien reservó)
    private Long            clientProfileId;
    private String          clientFullName;
}