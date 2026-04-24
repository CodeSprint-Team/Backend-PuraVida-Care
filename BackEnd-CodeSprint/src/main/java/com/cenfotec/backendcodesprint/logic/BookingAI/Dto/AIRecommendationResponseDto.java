package com.cenfotec.backendcodesprint.logic.BookingAI.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AIRecommendationResponseDto {

    private String category;
    private String service;
    private String reason;
    private String zone;

    //private Long careServiceId; // opcional para conectar con booking

    private boolean success;
    private String errorMessage;
}
