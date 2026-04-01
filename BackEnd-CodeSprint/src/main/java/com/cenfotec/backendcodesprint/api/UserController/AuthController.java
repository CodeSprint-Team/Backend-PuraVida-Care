package com.cenfotec.backendcodesprint.api.UserController;

import com.cenfotec.backendcodesprint.logic.Model.User;
import com.cenfotec.backendcodesprint.logic.User.Service.UserService;
import com.cenfotec.backendcodesprint.Security.JwtTokenProvider;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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

    @PostMapping("/google/callback")
    public ResponseEntity<?> googleCallback(@RequestBody Map<String, Object> request) {
        try {
            String accessToken = (String) request.get("token");
            Long roleId = request.get("roleId") != null
                    ? Long.valueOf(request.get("roleId").toString())
                    : null;

            if (accessToken == null || accessToken.isEmpty()) {
                return ResponseEntity.status(400).body(Map.of("error", "Token no proporcionado"));
            }

            // Verificar el access token con la API de Google
            URL url = new URL("https://www.googleapis.com/oauth2/v3/userinfo");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            System.out.println("Google userinfo response code: " + responseCode);

            if (responseCode != 200) {
                return ResponseEntity.status(401).body(Map.of("error", "Token de Google inválido"));
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            String jsonResponse = scanner.useDelimiter("\\A").next();
            scanner.close();

            System.out.println("Google userinfo response: " + jsonResponse);

            JsonObject userInfo = JsonParser.parseString(jsonResponse).getAsJsonObject();

            String email   = userInfo.get("email").getAsString();
            String name    = userInfo.has("name")       ? userInfo.get("name").getAsString()       : email;
            String given   = userInfo.has("given_name") ? userInfo.get("given_name").getAsString() : name;
            String picture = userInfo.has("picture")    ? userInfo.get("picture").getAsString()    : "";
            String subject = userInfo.has("sub")        ? userInfo.get("sub").getAsString()        : email;

            Object[] result = userService.createOrUpdateGoogleUser(email, name, given, subject, picture, roleId);
            User user      = (User) result[0];
            boolean isNew  = (boolean) result[1];

            System.out.println("User ID: " + user.getId() + " | isNewUser: " + isNew);

            if ("inactive".equalsIgnoreCase(user.getUserState())) {
                return ResponseEntity.status(403).body(Map.of("error", "Cuenta desactivada. Contactá al soporte."));
            }

            String jwtToken = jwtTokenProvider.generateToken(user);

            return ResponseEntity.ok(Map.of(
                    "token",     jwtToken,
                    "email",     user.getEmail(),
                    "name",      user.getUserName() + " " + user.getLastName(),
                    "userId",    user.getId(),
                    "role",      user.getRole().getRoleName(),
                    "isNewUser", isNew
            ));

        } catch (Exception e) {
            e.printStackTrace();
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
                Long userId  = jwtTokenProvider.getUserIdFromToken(token);

                response.put("valid", true);
                response.put("user", Map.of(
                        "email",  email,
                        "userId", userId
                ));
                return ResponseEntity.ok(response);
            }
        }

        response.put("valid", false);
        return ResponseEntity.status(401).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String email    = request.get("email");
            String password = request.get("password");

            User user = userService.loginWithEmailAndPassword(email, password);

            if ("inactive".equalsIgnoreCase(user.getUserState())) {
                return ResponseEntity.status(403).body(Map.of("error", "Cuenta desactivada. Contactá al soporte."));
            }

            String jwtToken = jwtTokenProvider.generateToken(user);

            return ResponseEntity.ok(Map.of(
                    "token",  jwtToken,
                    "email",  user.getEmail(),
                    "name",   user.getUserName() + " " + user.getLastName(),
                    "userId", user.getId(),
                    "role",   user.getRole().getRoleName()
            ));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
}