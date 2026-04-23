package com.cenfotec.backendcodesprint.logic.Telemedicina.Service;

import com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response.ClinicalAnalysisResponse;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response.McpToolResponse;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response.TranscriptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class McpClientService {

    private static final Logger log = LoggerFactory.getLogger(McpClientService.class);

    private final WebClient mcpWebClient;
    private final ObjectMapper objectMapper;

    @Value("${mcp.ai.timeout-seconds:30}")
    private int timeoutSeconds;

    public McpClientService(
            @Qualifier("mcpWebClient") WebClient mcpWebClient,
            ObjectMapper objectMapper
    ) {
        this.mcpWebClient = mcpWebClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Tool 1: Transcribir audio.
     */
    public TranscriptionResponse transcribeAudio(String audioBase64, String sessionId, String language) {
        Map<String, Object> body = new HashMap<>();
        body.put("audio_base64", audioBase64);
        body.put("session_id", sessionId);
        body.put("language", language != null ? language : "es");

        McpToolResponse mcpResponse = callMcpTool("/mcp/tools/transcribe_audio", body);
        return parseToolResponse(mcpResponse, TranscriptionResponse.class);
    }
    /**
     * Tool 2: Análisis clínico.
     */
    public ClinicalAnalysisResponse analyzeClinical(String transcript, String patientHistory, String sessionId) {
        Map<String, Object> body = new HashMap<>();
        body.put("transcript", transcript);
        body.put("session_id", sessionId);
        if (patientHistory != null && !patientHistory.isBlank()) {
            body.put("patient_history", patientHistory);
        }

        McpToolResponse mcpResponse = callMcpTool("/mcp/tools/analyze_clinical", body);
        return parseToolResponse(mcpResponse, ClinicalAnalysisResponse.class);
    }

    /**
     * Tool 3: Generar expediente médico.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> generateMedicalRecord(
            String transcript, String sessionId, String providerName, int durationMinutes
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("transcript", transcript);
        body.put("session_id", sessionId);
        body.put("provider_name", providerName != null ? providerName : "");
        body.put("session_duration_minutes", durationMinutes);

        McpToolResponse mcpResponse = callMcpTool("/mcp/tools/generate_medical_record", body);
        return parseToolResponse(mcpResponse, Map.class);
    }
    /**
     * Health check del microservicio de IA.
     */
    public boolean isHealthy() {
        try {
            Map<?, ?> response = mcpWebClient.get()
                    .uri("/health")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            return response != null && "healthy".equals(response.get("status"));
        } catch (Exception e) {
            log.error("MCP Server no disponible: {}", e.getMessage());
            return false;
        }
    }

    // ============================================================
    // Métodos internos de comunicación
    // ============================================================

    private McpToolResponse callMcpTool(String uri, Map<String, Object> body) {
        try {
            McpToolResponse response = mcpWebClient.post()
                    .uri(uri)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(McpToolResponse.class)
                    .timeout(Duration.ofSeconds(timeoutSeconds))
                    .block();

            if (response == null) {
                throw new RuntimeException("Respuesta nula del MCP Server");
            }
            if (response.isError()) {
                throw new RuntimeException("Error en MCP tool: " + uri);
            }
            return response;
        } catch (Exception e) {
            log.error("Error llamando MCP tool {}: {}", uri, e.getMessage());
            throw new RuntimeException("Error en comunicación con servicio de IA: " + e.getMessage(), e);
        }
    }

    private <T> T parseToolResponse(McpToolResponse mcpResponse, Class<T> targetClass) {
        try {
            if (mcpResponse.getContent() == null || mcpResponse.getContent().isEmpty()) {
                throw new RuntimeException("Respuesta MCP sin contenido");
            }
            String jsonText = mcpResponse.getContent().get(0).getText();
            return objectMapper.readValue(jsonText, targetClass);
        } catch (JsonProcessingException e) {
            log.error("Error parseando respuesta MCP: {}", e.getMessage());
            throw new RuntimeException("Error procesando respuesta de IA: " + e.getMessage(), e);
        }
    }

}

