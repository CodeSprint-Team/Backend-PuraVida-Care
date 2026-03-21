package com.cenfotec.backendcodesprint.api.UserController;

import com.cenfotec.backendcodesprint.logic.Model.User;
import com.cenfotec.backendcodesprint.logic.User.Service.UserService;
import com.cenfotec.backendcodesprint.Security.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Backend funcionando correctamente");
    }

    @GetMapping("/google/url")
    public ResponseEntity<Map<String, String>> getGoogleAuthUrl() {
        Map<String, String> response = new HashMap<>();
        response.put("url", "http://localhost:8081/api/v1/auth/login/oauth2/code/google");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/google/callback")
    public ResponseEntity<?> googleCallback(@RequestBody Map<String, String> request) {
        try {
            System.out.println("Google Client ID: " + googleClientId);
            System.out.println("Token recibido: " + request.get("token"));
            // 1. El frontend envía el token de Google en el body
            String googleToken = request.get("token");

            // 2. Verificar el token con Google
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(googleToken);

            if (idToken == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Token de Google inválido"));
            }

            // 3. Obtener información del usuario de Google
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String picture = (String) payload.get("picture");
            String givenName = (String) payload.get("given_name");
            String subject = payload.getSubject();

            // 4. Buscar o crear usuario en tu base de datos
            User user = userService.createOrUpdateGoogleUser(
                    email,
                    name,
                    givenName != null ? givenName : name.split(" ")[0],
                    subject,
                    picture
            );
               // Bloqueo: admin desactivó al usuario
            if
            ("inactive".equalsIgnoreCase(user.getUserState())) {
                return
                        ResponseEntity.status(403).body(Map.of("error",
                                "Cuenta desactivada. Contactá al soporte."));
            }
            // 5. Generar tu propio JWT
            String jwtToken = jwtTokenProvider.generateToken(user);

            // 6. Responder con el token y datos del usuario
            return ResponseEntity.ok(Map.of(
                    "token", jwtToken,
                    "email", user.getEmail(),
                    "name", user.getUserName() + " " + user.getLastName(),
                    "userId", user.getId(),
                    "role", user.getRole().getRoleName()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error en autenticación: " + e.getMessage()));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getEmailFromToken(token);
                Long userId = jwtTokenProvider.getUserIdFromToken(token);

                response.put("valid", true);
                response.put("user", Map.of(
                        "email", email,
                        "userId", userId
                ));
                return ResponseEntity.ok(response);
            }
        }

        response.put("valid", false);
        return ResponseEntity.status(401).body(response);
    }



    @GetMapping("/debug/env")
    public ResponseEntity<Map<String, String>> debugEnv() {
        Map<String, String> response = new HashMap<>();
        response.put("GOOGLE_CLIENT_ID", googleClientId);
        response.put("GOOGLE_CLIENT_ID_ENV", System.getenv("GOOGLE_CLIENT_ID"));
        return ResponseEntity.ok(response);
    }
}