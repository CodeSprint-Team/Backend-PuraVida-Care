package com.cenfotec.backendcodesprint.logic.BookingAI.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AIRecommendationRequestDto {

    @NotBlank(message = "La necesidad del usuario es requerida")
    private String need;

    @NotBlank(message = "La fecha es requerida")
    private String date;

    private String mobility;

    private String companion;

    @NotBlank(message = "La zona es requerida")
    private String zone;
}

