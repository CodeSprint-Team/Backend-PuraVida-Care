package com.cenfotec.backendcodesprint.logic.BookingAI.Service;


import com.cenfotec.backendcodesprint.logic.BookingAI.Dto.AIRecommendationRequestDto;
import com.cenfotec.backendcodesprint.logic.BookingAI.Dto.AIRecommendationResponseDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class RecommendationService {

    private final WebClient recommendationWebClient;

    public RecommendationService(@Qualifier("recommendationWebClient") WebClient recommendationWebClient) {
        this.recommendationWebClient = recommendationWebClient;
    }

    public AIRecommendationResponseDto getRecommendation(AIRecommendationRequestDto req) {

        if (isBlank(req.getNeed()) || isBlank(req.getZone())) {
            return error("Para recomendarte un servicio necesito conocer tu necesidad y la zona.");
        }

        List<String> availableCategories = getAvailableCategories();

        RecommendPythonRequest pythonRequest = new RecommendPythonRequest(
                req.getNeed(),
                req.getDate()      != null ? req.getDate()      : "",
                req.getMobility()  != null ? req.getMobility()  : "",
                req.getCompanion() != null ? req.getCompanion() : "",
                req.getZone(),
                availableCategories
        );

        try {
            RecommendPythonResponse pythonResponse = recommendationWebClient
                    .post()
                    .uri("/ai/recommend")
                    .bodyValue(pythonRequest)
                    .retrieve()
                    .bodyToMono(RecommendPythonResponse.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            if (pythonResponse == null || !pythonResponse.success) {
                String msg = (pythonResponse != null && pythonResponse.errorMessage != null)
                        ? pythonResponse.errorMessage
                        : "No se pudo generar la recomendación. Intenta nuevamente.";
                return error(msg);
            }

            log.info("Recomendación IA generada: categoria='{}' zona='{}'",
                    pythonResponse.category, req.getZone());

            return AIRecommendationResponseDto.builder()
                    .category(pythonResponse.category)
                    .service(pythonResponse.service)
                    .reason(pythonResponse.reason)
                    .zone(pythonResponse.zone)
                    .success(true)
                    .build();

        } catch (Exception e) {
            // TC-177: Falla técnica del microservicio IA
            log.error("Error llamando al microservicio de recomendación: {}", e.getMessage());
            return error("No se pudo generar la recomendación, intenta nuevamente.");
        }
    }

    private List<String> getAvailableCategories() {
        return List.of(
                "Enfermería", "Fisioterapia", "Transporte",
                "Acompañamiento", "Cuidado general", "Compras", "Trámite",
                "Transporte Seguro Adulto mayor", "Telemedicina", "Enfermero"
        );
    }

    private AIRecommendationResponseDto error(String message) {
        return AIRecommendationResponseDto.builder()
                .success(false)
                .errorMessage(message)
                .build();
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    @Data
    private static class RecommendPythonRequest {
        private final String       need;
        private final String       date;
        private final String       mobility;
        private final String       companion;
        private final String       zone;
        @JsonProperty("available_categories")
        private final List<String> availableCategories;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class RecommendPythonResponse {
        private String  category;
        private String  service;
        private String  reason;
        private String  zone;
        private boolean success;
        @JsonProperty("errorMessage")
        private String  errorMessage;
    }
}