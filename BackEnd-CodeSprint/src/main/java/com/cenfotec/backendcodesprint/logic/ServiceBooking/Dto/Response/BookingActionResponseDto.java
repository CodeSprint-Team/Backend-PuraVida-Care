package com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingActionResponseDto {

    private Long bookingId;
    private String trackingState;
    private String message;
}
