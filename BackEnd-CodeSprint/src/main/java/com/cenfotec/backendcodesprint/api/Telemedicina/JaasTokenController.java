package com.cenfotec.backendcodesprint.api.Telemedicina;

import com.cenfotec.backendcodesprint.logic.Telemedicina.Service.JaasJwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/telemedicina-controll")
@AllArgsConstructor
public class JaasTokenController {

    private final JaasJwtService jaasJwtService;

    /**
     * GET /telemedicina-controll/jaas-token?sessionId=1&userName=Doctor&userEmail=doc@mail.com&moderator=true
     *
     * Genera un JWT para que un participante se una a una sala de JaaS.
     * El doctor es moderator=true, el paciente moderator=false.
     */
    @GetMapping("/jaas-token")
    public ResponseEntity<Map<String, String>> getJaasToken(
            @RequestParam String sessionId,
            @RequestParam String userName,
            @RequestParam(required = false, defaultValue = "") String userEmail,
            @RequestParam(defaultValue = "false") boolean moderator
    ) {
        String roomName = "telemed-session-" + sessionId;

        String jwt = jaasJwtService.generateToken(roomName, userName, userEmail, moderator);

        return ResponseEntity.ok(Map.of(
                "token", jwt,
                "roomName", roomName,
                "domain", "8x8.vc",
                "appId", jaasJwtService.getAppId()
        ));
    }
}