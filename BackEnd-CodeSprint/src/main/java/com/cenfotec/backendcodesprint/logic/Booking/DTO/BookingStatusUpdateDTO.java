package com.cenfotec.backendcodesprint.logic.Booking.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BookingStatusUpdateDTO {

    @NotBlank
    @Pattern(regexp = "accepted|rejected", message = "Status must be 'accepted' or 'rejected'")
    private String status;
    private String rejectionReason;
}
