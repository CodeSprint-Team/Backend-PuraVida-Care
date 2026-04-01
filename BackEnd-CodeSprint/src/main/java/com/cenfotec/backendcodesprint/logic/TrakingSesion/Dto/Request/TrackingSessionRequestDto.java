package com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackingSessionRequestDto {

    @NotNull(message = "El bookingId es obligatorio")
    private Long bookingId;
}
