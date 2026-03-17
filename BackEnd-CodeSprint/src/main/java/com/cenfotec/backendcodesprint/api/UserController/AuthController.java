package com.cenfotec.backendcodesprint.api.UserController;

import com.cenfotec.backendcodesprint.logic.User.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
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
        Map<String, Object> response = new HashMap<>();
        response.put("token", "dummy-jwt-token");
        response.put("email", "usuario@ejemplo.com");
        response.put("name", "Usuario Google");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            response.put("valid", true);
            response.put("user", Map.of("email", "usuario@ejemplo.com"));
            return ResponseEntity.ok(response);
        }
        response.put("valid", false);
        return ResponseEntity.status(401).body(response);
    }
}