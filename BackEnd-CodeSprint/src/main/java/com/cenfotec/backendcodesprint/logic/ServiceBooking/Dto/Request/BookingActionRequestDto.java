package com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingActionRequestDto {

    @NotBlank(message = "La accion es obligatoria")
    @Pattern(regexp = "ACEPTAR|RECHAZAR", message = "La accion debde der ser ACEPTAR o RECHAZAR")
    private String action;
}
