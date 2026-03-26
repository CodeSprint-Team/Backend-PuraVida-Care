package com.cenfotec.backendcodesprint.logic.SimulatorTracking.Dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimulatrRouterRequest {

    @NotNull(message = "Los waypoint son requeidos")
    @Size(min =2, message = "Se nesecitan al menos 2 waypoint (origen y destino)")
    private List<double[]> waypoints;

    @Min(value = 2, message = "Mínimo 2 puntos por segmento")
    private int pointsPerSegment = 15;

    @Min(value = 200, message = "Intervalo mínimo 200ms")
    private long intervalMs = 1500;

}
