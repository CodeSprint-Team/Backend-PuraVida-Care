package com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackingPointRequestDto {

    @NotNull(message = "La latitud es obligatoria")
    @DecimalMin(value = "-90.0",  message = "Latitud mínima -90")
    @DecimalMax(value = "90.0",   message = "Latitud máxima 90")
    private Double latitude;

    @NotNull(message = "La longitud es obligatoria")
    @DecimalMin(value = "-180.0", message = "Longitud mínima -180")
    @DecimalMax(value = "180.0",  message = "Longitud máxima 180")
    private Double longitude;
}