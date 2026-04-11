package com.cenfotec.backendcodesprint.logic.Telemedicina.Mapper;

import com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response.ConsentResponse;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response.EndSessionResponse;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response.TranscriptResponse;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response.AiHealthResponse;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Enums.AiStatus;
import org.mapstruct.Mapper;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface TelemedMapper {

    // --- Consent ---
    default ConsentResponse toConsentResponse(String sessionId, boolean accepted, String consentAt) {
        return ConsentResponse.builder()
                .sessionId(sessionId)
                .aiActive(accepted)
                .consentAt(consentAt)
                .message(accepted
                        ? "IA activada — se iniciará transcripción cuando comience el audio"
                        : "IA desactivada — la sesión continuará sin asistencia de IA")
                .build();
    }

    // --- Deactivate AI ---
    default ConsentResponse toDeactivateResponse(String sessionId) {
        return ConsentResponse.builder()
                .sessionId(sessionId)
                .aiActive(false)
                .message("IA desactivada por el profesional médico")
                .build();
    }

    default EndSessionResponse toEndSessionResponse(
            String sessionId,
            boolean hasRecord,
            Map<String, Object> record,
            AiStatus aiStatus
    ) {
        return EndSessionResponse.builder()
                .sessionId(sessionId)
                .hasRecord(hasRecord)
                .record(record)
                .iaStatus(aiStatus.name())
                .message(buildEndSessionMessage(hasRecord, aiStatus))
                .build();
    }

    default EndSessionResponse toEndSessionErrorResponse(String sessionId, String errorMessage) {
        return EndSessionResponse.builder()
                .sessionId(sessionId)
                .hasRecord(false)
                .iaStatus(AiStatus.UNAVAILABLE.name())
                .message("Error generando expediente: " + errorMessage)
                .build();
    }

    default String buildEndSessionMessage(boolean hasRecord, AiStatus aiStatus) {
        return switch (aiStatus) {
            case COMPLETED -> "Expediente generado con análisis de IA completo";
            case PARTIAL -> "Expediente generado sin transcripción disponible";
            case UNAVAILABLE -> "Expediente generado — análisis de IA no disponible, puede reintentarse";
            case NOT_CONSENTED -> "Sesión finalizada sin asistencia de IA";
            case DEACTIVATED -> "Expediente generado — IA fue desactivada por el profesional";
            case ACTIVE -> "Sesión en curso";
        };
    }

    // --- Transcript ---
    default TranscriptResponse toTranscriptResponse(String sessionId, String transcript, boolean aiActive) {
        return TranscriptResponse.builder()
                .sessionId(sessionId)
                .transcript(transcript)
                .aiActive(aiActive)
                .build();
    }

    // --- AI Health ---
    default AiHealthResponse toAiHealthResponse(boolean healthy) {
        return AiHealthResponse.builder()
                .aiService(healthy ? "available" : "unavailable")
                .message(healthy
                        ? "Microservicio de IA funcionando correctamente"
                        : "Microservicio de IA no disponible — telemedicina funcionará sin IA")
                .build();
    }
}
