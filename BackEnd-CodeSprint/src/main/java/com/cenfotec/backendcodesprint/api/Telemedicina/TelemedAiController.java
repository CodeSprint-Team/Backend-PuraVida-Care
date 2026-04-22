package com.cenfotec.backendcodesprint.api.Telemedicina;

import com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Request.ConsentRequest;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Request.EndSessionRequest;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response.AiHealthResponse;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response.ConsentResponse;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response.EndSessionResponse;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response.TranscriptResponse;
import com.cenfotec.backendcodesprint.logic.Telemedicina.Service.TelemedAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/telemedicina-controll")
public class TelemedAiController {

    private final TelemedAiService service;

    public TelemedAiController(TelemedAiService service) {
        this.service = service;
    }

    @PutMapping("/telemed-sessions/{sessionId}/consent")
    public ResponseEntity<ConsentResponse> registerConsent(
            @PathVariable String sessionId,
            @RequestBody ConsentRequest request
    ) {
        return ResponseEntity.ok(service.registerConsent(sessionId, request.isAccepted()));
    }

    @PutMapping("/telemed-sessions/{sessionId}/deactivate-ai")
    public ResponseEntity<ConsentResponse> deactivateAi(@PathVariable String sessionId) {
        return ResponseEntity.ok(service.deactivateAi(sessionId));
    }

    @PostMapping("/telemed-sessions/{sessionId}/end")
    public ResponseEntity<EndSessionResponse> endSession(
            @PathVariable String sessionId,
            @RequestBody(required = false) EndSessionRequest request
    ) {
        String providerName = request != null ? request.getProviderName() : "";
        int duration = request != null ? request.getDurationMinutes() : 0;
        return ResponseEntity.ok(service.endSession(sessionId, providerName, duration));
    }

    @GetMapping("/telemed-sessions/{sessionId}/transcript")
    public ResponseEntity<TranscriptResponse> getTranscript(@PathVariable String sessionId) {
        return ResponseEntity.ok(service.getTranscript(sessionId));
    }

    @GetMapping("/telemed-ai/health")
    public ResponseEntity<AiHealthResponse> aiHealthCheck() {
        return ResponseEntity.ok(service.checkAiHealth());
    }
}
