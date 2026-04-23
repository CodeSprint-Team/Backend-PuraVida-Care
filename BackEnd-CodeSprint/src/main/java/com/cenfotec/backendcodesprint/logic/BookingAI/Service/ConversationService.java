package com.cenfotec.backendcodesprint.logic.BookingAI.Service;

import com.cenfotec.backendcodesprint.logic.BookingAI.Dto.ChatConversationRequestDto;
import com.cenfotec.backendcodesprint.logic.BookingAI.Dto.ChatConversationResponseDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ConversationService {

    private final WebClient recommendationWebClient;

    public ConversationService(@Qualifier("recommendationWebClient") WebClient recommendationWebClient) {
        this.recommendationWebClient = recommendationWebClient;
    }

    public ChatConversationResponseDto chat(ChatConversationRequestDto req) {

        if (req.getHistory() == null || req.getHistory().isEmpty()) {
            return errorResponse("El historial de conversación está vacío.");
        }

        // Construir request para el microservicio Python
        Map<String, Object> pythonRequest = Map.of(
                "history", req.getHistory().stream()
                        .map(t -> Map.of("role", t.getRole(), "content", t.getContent()))
                        .toList(),
                "availableCategories", req.getAvailableCategories() != null
                        ? req.getAvailableCategories()
                        : getDefaultCategories()
        );

        try {
            PythonChatResponse pythonResponse = recommendationWebClient
                    .post()
                    .uri("/ai/chat")
                    .bodyValue(pythonRequest)
                    .retrieve()
                    .bodyToMono(PythonChatResponse.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            if (pythonResponse == null || !pythonResponse.success) {
                return errorResponse("No se pudo procesar el mensaje. Intentá nuevamente.");
            }

            if ("recommendation".equals(pythonResponse.type) && pythonResponse.recommendation != null) {
                log.info("Recomendación conversacional generada: {}",
                        pythonResponse.recommendation.get("category"));

                return ChatConversationResponseDto.builder()
                        .type("recommendation")
                        .recommendation(ChatConversationResponseDto.RecommendationDto.builder()
                                .category((String) pythonResponse.recommendation.get("category"))
                                .service((String)  pythonResponse.recommendation.get("service"))
                                .reason((String)   pythonResponse.recommendation.get("reason"))
                                .zone((String)     pythonResponse.recommendation.get("zone"))
                                .build())
                        .success(true)
                        .build();

            } else {
                return ChatConversationResponseDto.builder()
                        .type("message")
                        .message(pythonResponse.message)
                        .success(true)
                        .build();
            }

        } catch (Exception e) {
            log.error("Error en ConversationService: {}", e.getMessage());
            return errorResponse("No se pudo procesar tu mensaje. Intentá nuevamente.");
        }
    }

    private List<String> getDefaultCategories() {
        return List.of(
                "Enfermería", "Fisioterapia", "Transporte",
                "Acompañamiento", "Cuidado general", "Compras", "Trámite",
                "Transporte Seguro Adulto mayor", "Telemedicina", "Enfermero"
        );
    }

    private ChatConversationResponseDto errorResponse(String message) {
        return ChatConversationResponseDto.builder()
                .type("message")
                .message(message)
                .success(false)
                .errorMessage(message)
                .build();
    }

    // ── DTO interno para parsear respuesta del Python ──────
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class PythonChatResponse {
        private String              type;
        private String              message;
        @JsonProperty("recommendation")
        private Map<String, Object> recommendation;
        private boolean             success;
        private String              errorMessage;
    }
}