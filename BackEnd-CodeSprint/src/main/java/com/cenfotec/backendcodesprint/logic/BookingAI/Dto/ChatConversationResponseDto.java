package com.cenfotec.backendcodesprint.logic.BookingAI.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatConversationResponseDto {

    // 'message'        → la IA sigue conversando
    // 'recommendation' → la IA tiene suficiente info y recomienda
    private String type;

    // Solo presente cuando type = 'message'
    private String message;

    // Solo presente cuando type = 'recommendation'
    private RecommendationDto recommendation;

    private boolean success;
    private String  errorMessage;

    @Data
    @Builder
    public static class RecommendationDto {
        private String category;
        private String service;
        private String reason;
        private String zone;
    }
}