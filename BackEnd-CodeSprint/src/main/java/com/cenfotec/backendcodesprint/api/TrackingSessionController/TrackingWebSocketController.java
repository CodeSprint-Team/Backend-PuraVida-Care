package com.cenfotec.backendcodesprint.api.TrackingSessionController;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TrackingWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Recibe un punto del proveedor y lo reenvía al familiar.
     */
    @MessageMapping("/tracking/{sessionId}/point")
    @SendTo("/topic/tracking/{sessionId}")
    public Map<String, Object> sendPoint(
            @DestinationVariable Long sessionId,
            Map<String, Object> point) {

        if (!point.containsKey("timestamp")) {
            point.put("timestamp", System.currentTimeMillis());
        }
        point.put("sessionId", sessionId);

        return point;
    }

    /**
     * Método para enviar puntos desde el backend (usado por el simulador).
     */
    public void broadcastPoint(Long sessionId, double latitude, double longitude) {
        Map<String, Object> point = Map.of(
                "sessionId", sessionId,
                "latitude", latitude,
                "longitude", longitude,
                "timestamp", System.currentTimeMillis()
        );
        messagingTemplate.convertAndSend("/topic/tracking/" + sessionId, point);
    }
}
