package com.cenfotec.backendcodesprint.logic.Booking.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingResponseDTO {

    private Long id;

    //  Cliente

    private Long clientProfileId;
    private String clientName;
    private String clientPhone;

    // Senior

    private Long seniorProfileId;
    private String seniorName;

    //  Service

    private Long careServiceId;
    private String ServiceName;
    private String ServiceCategory;

    //  Reserva

    private LocalDateTime scheduledAt;
    private BigDecimal agreedPrice;
    private String agreedPriceMode;

    //  Status

    private String bookingStatus;
    private String rejectionReason;

    //  Dates

    private LocalDateTime requestDate;
}
