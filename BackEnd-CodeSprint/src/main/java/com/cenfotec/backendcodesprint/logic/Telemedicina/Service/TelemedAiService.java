package com.cenfotec.backendcodesprint.logic.Telemedicina.Service;

import com.cenfotec.backendcodesprint.logic.ClinicalRecord.Enums.EntryType;
import com.cenfotec.backendcodesprint.logic.ClinicalRecord.Service.ClinicalRecordService;
import com.cenfotec.backendcodesprint.logic.Model.ClinicalRecord;
import com.cenfotec.backendcodesprint.logic.Model.TelemedSession;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response.*;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Enums.AiStatus;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Mapper.TelemedMapper;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Repository.TelemedAiRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Service
public class TelemedAiService {

    private static final Logger log = LoggerFactory.getLogger(TelemedAiService.class);

    private final McpClientService mcpClient;
    private final TelemedMapper mapper;
    private final TelemedAiRepository repository;
    private final Map<String, SessionAiState> activeSessions = new ConcurrentHashMap<>();
    private final ClinicalRecordService clinicalRecordService;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messaging;



    // ============================================================
    // CONSENTIMIENTO
    // ============================================================

    @Transactional
    public ConsentResponse registerConsent(String sessionId, boolean accepted) {
        String consentAt = OffsetDateTime.now().toString();

        if (accepted) {
            activeSessions.put(sessionId, new SessionAiState());
            log.info("IA activada para sesión {} — paciente dio consentimiento", sessionId);
        } else {
            activeSessions.remove(sessionId);
            log.info("IA NO activada para sesión {} — paciente rechazó", sessionId);
        }

        try {
            Long id = Long.parseLong(sessionId);
            repository.updateAiConsent(id, accepted, OffsetDateTime.now());
        } catch (NumberFormatException e) {
            log.warn("SessionId {} no es numérico — consent guardado solo en memoria", sessionId);
        }

        return mapper.toConsentResponse(sessionId, accepted, consentAt);
    }

    public ConsentResponse deactivateAi(String sessionId) {
        SessionAiState state = activeSessions.get(sessionId);
        if (state != null) {
            state.aiEnabled = false;
        }
        log.info("IA desactivada manualmente para sesión {}", sessionId);
        return mapper.toDeactivateResponse(sessionId);
    }

    private static class SessionAiState {
        private final StringBuilder transcript = new StringBuilder();
        private boolean aiEnabled = true;

        synchronized void appendTranscript(String text) {
            if (transcript.length() > 0) transcript.append(" ");
            transcript.append(text);
        }

        synchronized String getTranscript() {
            return transcript.toString();
        }
    }

    public boolean isAiActive(String sessionId) {
        return activeSessions.containsKey(sessionId);
    }


    public TranscriptionResponse processAudioChunk(String sessionId, String audioBase64) {
        SessionAiState state = activeSessions.get(sessionId);
        if (state == null || !state.aiEnabled) {
            log.warn("Audio recibido para sesión {} sin consentimiento o IA desactivada", sessionId);
            return null;
        }

        TranscriptionResponse response = mcpClient.transcribeAudio(audioBase64, sessionId, "es");

        if (response != null && response.getCleanText() != null && !response.getCleanText().isBlank()) {
            state.appendTranscript(response.getCleanText());
        }

        return response;
    }

    // ============================================================
    // ANÁLISIS CLÍNICO (llamado desde WebSocket controller)
    // ============================================================

    public ClinicalAnalysisResponse requestAnalysis(String sessionId, String patientHistory) {
        SessionAiState state = activeSessions.get(sessionId);
        if (state == null) {
            log.warn("Análisis solicitado para sesión {} sin consentimiento", sessionId);
            return null;
        }

        String transcriptText = state.getTranscript();

        if (transcriptText.isBlank()) {
            log.warn("Análisis solicitado para sesión {} pero transcripción vacía", sessionId);
            return null;
        }

        return mcpClient.analyzeClinical(transcriptText, patientHistory, sessionId);
    }

    // ============================================================
    // FINALIZAR SESIÓN Y GENERAR EXPEDIENTE
    // ============================================================

    public EndSessionResponse endSession(String sessionId, String providerName, int durationMinutes) {

        TelemedSession session = repository.findById(Long.parseLong(sessionId))
                .orElseThrow(() -> new RuntimeException("No se encontró la sesión " + sessionId));

        SessionAiState state = activeSessions.get(sessionId);
        String transcriptText = state != null ? state.getTranscript() : "";

        ClinicalRecord clinicalRecord = clinicalRecordService.findOrCreateRecord(
                session.getSeniorProfile()
        );

        if (!transcriptText.isBlank()) {
            clinicalRecordService.addEntry(
                    clinicalRecord,
                    EntryType.TRANSCRIPTION,
                    transcriptText,
                    session.getProviderProfile().getUser()
            );
        }
        AiStatus finalStatus;
        Map<String, Object> aiRecord = null;

        if (!session.getAiConsent()) {
            finalStatus = AiStatus.NOT_CONSENTED;
        } else if (transcriptText.isBlank()) {
            finalStatus = AiStatus.PARTIAL;
        } else {
            try {
                aiRecord = mcpClient.generateMedicalRecord(
                        transcriptText, sessionId, providerName, durationMinutes
                );
                clinicalRecordService.saveAiAnalysis(
                        clinicalRecord,
                        session,
                        session.getProviderProfile(),
                        transcriptText,
                        "completed",
                        objectMapper.writeValueAsString(aiRecord)
                );
                finalStatus = AiStatus.COMPLETED;
            } catch (Exception e) {
                log.warn("IA no disponible para la sesión {}: {}", sessionId, e.getMessage());
                finalStatus = AiStatus.UNAVAILABLE;
            }
        }

        session.setSessionState("completed");
        session.setEndedAt(OffsetDateTime.now());
        session.setAiStatus(finalStatus);
        repository.save(session);

        activeSessions.remove(sessionId);

        boolean hasRecord = !transcriptText.isBlank() || aiRecord != null;
        return mapper.toEndSessionResponse(sessionId, hasRecord, aiRecord, finalStatus);
    }

    public void handleAudioChunk(String sessionId, String audioBase64) {
        if (audioBase64 == null || audioBase64.isBlank()) return;
        if (!isAiActive(sessionId)) return;

        try {
            TranscriptionResponse result = processAudioChunk(sessionId, audioBase64);

            if (result != null && result.getCleanText() != null && !result.getCleanText().isBlank()) {
                messaging.convertAndSend("/topic/telemed/" + sessionId + "/transcription", result);
            }
        } catch (Exception e) {
            messaging.convertAndSend(
                    "/topic/telemed/" + sessionId + "/error",
                    Map.of("message", "Error procesando audio: " + e.getMessage())
            );
        }
    }

    public void handleAnalysisRequest(String sessionId, String patientHistory) {
        if (!isAiActive(sessionId)) return;

        try {
            messaging.convertAndSend(
                    "/topic/telemed/" + sessionId + "/analysis-status",
                    Map.of("status", "processing")
            );

            ClinicalAnalysisResponse analysis = requestAnalysis(sessionId, patientHistory);

            if (analysis != null) {
                messaging.convertAndSend("/topic/telemed/" + sessionId + "/analysis", analysis);
            }
        } catch (Exception e) {
            messaging.convertAndSend(
                    "/topic/telemed/" + sessionId + "/error",
                    Map.of("message", "Error en análisis: " + e.getMessage())
            );
        }
    }
    // ============================================================
    // CONSULTAS
    // ============================================================

    public TranscriptResponse getTranscript(String sessionId) {
        SessionAiState state = activeSessions.get(sessionId);
        String text = state != null ? state.getTranscript() : "";
        return mapper.toTranscriptResponse(sessionId, text, isAiActive(sessionId));
    }

    public AiHealthResponse checkAiHealth() {
        boolean healthy = mcpClient.isHealthy();
        return mapper.toAiHealthResponse(healthy);
    }
}
