package com.cenfotec.backendcodesprint.api.Telemedicina;

import com.cenfotec.backendcodesprint.logic.Telemedicina.Service.TelemedAiService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class TelemedWebSocketController {

    private final TelemedAiService service;

    public TelemedWebSocketController(TelemedAiService service) {
        this.service = service;
    }

    @MessageMapping("/telemed/{sessionId}/audio")
    public void handleAudioChunk(
            @DestinationVariable String sessionId,
            @Payload Map<String, String> payload
    ) {
        service.handleAudioChunk(sessionId, payload.get("audioBase64"));
    }

    @MessageMapping("/telemed/{sessionId}/analyze")
    public void handleAnalysisRequest(
            @DestinationVariable String sessionId,
            @Payload Map<String, String> payload
    ) {
        service.handleAnalysisRequest(sessionId, payload.get("patientHistory"));
    }
}