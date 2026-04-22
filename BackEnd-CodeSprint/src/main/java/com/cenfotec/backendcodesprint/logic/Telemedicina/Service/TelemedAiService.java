package com.cenfotec.backendcodesprint.logic.Telemedicina.Service;

import com.cenfotec.backendcodesprint.logic.ClinicalRecord.Enums.EntryType;
import com.cenfotec.backendcodesprint.logic.ClinicalRecord.Repository.AiClinicalAnalysisRepository;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private final AiClinicalAnalysisRepository aiClinicalAnalysisRepository;


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

    @Transactional
    public EndSessionResponse endSession(String sessionId, String providerName, int durationMinutes) {

        log.info("[endSession] Iniciando cierre de sesión {}", sessionId);

        // ─── 1. Buscar la sesión ───
        TelemedSession session = repository.findById(Long.parseLong(sessionId))
                .orElseThrow(() -> new RuntimeException("No se encontró la sesión " + sessionId));

        SessionAiState state = activeSessions.get(sessionId);
        String transcriptText = state != null ? state.getTranscript() : "";

        log.info("[endSession] Transcripción obtenida, length = {}, aiConsent = {}",
                transcriptText.length(), session.getAiConsent());

        // ─── 2. Buscar o crear expediente del senior ───
        ClinicalRecord clinicalRecord = clinicalRecordService.findOrCreateRecord(
                session.getSeniorProfile()
        );

        log.info("[endSession] ClinicalRecord obtenido, id = {}", clinicalRecord.getId());

        // ─── 3. Guardar transcripción (SIEMPRE, aunque la IA falle) ───
        if (!transcriptText.isBlank()) {
            clinicalRecordService.addEntry(
                    clinicalRecord,
                    EntryType.TRANSCRIPTION,
                    transcriptText,
                    session.getProviderProfile().getUser()
            );
            log.info("[endSession] Entry TRANSCRIPTION guardada");
        }

        // ─── 4. Intentar análisis IA ───
        AiStatus finalStatus;
        Map<String, Object> aiRecord = null;

        if (!session.getAiConsent()) {
            finalStatus = AiStatus.NOT_CONSENTED;
            log.info("[endSession] Status final: NOT_CONSENTED (sin consentimiento)");
        } else if (transcriptText.isBlank()) {
            finalStatus = AiStatus.PARTIAL;
            log.info("[endSession] Status final: PARTIAL (sin transcripción)");
        } else {
            try {
                log.info("[endSession] Llamando a IA para generar expediente...");
                aiRecord = mcpClient.generateMedicalRecord(
                        transcriptText, sessionId, providerName, durationMinutes
                );
                log.info("[endSession] ✅ IA respondió OK, aiRecord keys = {}",
                        aiRecord != null ? aiRecord.keySet() : "null");

                String payloadJson = objectMapper.writeValueAsString(aiRecord);
                log.info("[endSession] JSON serializado, length = {}", payloadJson.length());

                var saved = clinicalRecordService.saveAiAnalysis(
                        clinicalRecord,
                        session,
                        session.getProviderProfile(),
                        transcriptText,
                        "completed",
                        payloadJson
                );
                log.info("[endSession] ✅ saveAiAnalysis OK, id generado = {}",
                        saved != null ? saved.getId() : "null");

                finalStatus = AiStatus.COMPLETED;
            } catch (Exception e) {
                log.error("[endSession] ❌ Error en el bloque IA: {}", e.getMessage(), e);
                finalStatus = AiStatus.UNAVAILABLE;
            }
        }

        // ─── 5. Cerrar sesión ───
        session.setSessionState("completed");
        session.setEndedAt(OffsetDateTime.now());
        session.setAiStatus(finalStatus);
        repository.save(session);
        log.info("[endSession] Sesión {} cerrada con status {}", sessionId, finalStatus);

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

    public List<Map<String, Object>> getSessionHistory(Long seniorProfileId) {
        List<TelemedSession> sessions = repository.findBySeniorProfileIdOrderByEndedAtDesc(seniorProfileId);

        List<Map<String, Object>> result = new ArrayList<>();

        for (TelemedSession session : sessions) {
            if (!"completed".equals(session.getSessionState())) continue;

            Map<String, Object> item = new HashMap<>();
            item.put("sessionId", session.getId());
            item.put("startedAt", session.getStartedAt());
            item.put("endedAt", session.getEndedAt());
            item.put("aiStatus", session.getAiStatus());

            var analysis = aiClinicalAnalysisRepository
                    .findFirstByTelemedSessionIdOrderByCreatedDesc(session.getId());

            if (analysis.isPresent()) {
                try {
                    Map<String, Object> payload = objectMapper.readValue(
                            analysis.get().getPayloadWithoutPii(),
                            new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {
                            }
                    );
                    item.put("record", payload);
                } catch (Exception e) {
                    log.warn("Error parseando payload para sesión {}", session.getId());
                    item.put("record", null);
                }
            } else {
                item.put("record", null);
            }

            result.add(item);
        }

        return result;
    }

}
