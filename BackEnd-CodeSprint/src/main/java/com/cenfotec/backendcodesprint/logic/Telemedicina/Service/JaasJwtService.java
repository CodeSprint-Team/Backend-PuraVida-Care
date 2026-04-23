package com.cenfotec.backendcodesprint.logic.Telemedicina.Service;

import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.*;

@Service
public class JaasJwtService {

    private static final Logger log = LoggerFactory.getLogger(JaasJwtService.class);

    @Value("${jaas.app-id}")
    private String appId;

    @Value("${jaas.key-id}")
    private String keyId;

    @Value("${jaas.private-key-path}")
    private Resource privateKeyResource;

    private RSAPrivateKey privateKey;

    @PostConstruct
    public void init() {
        try {
            String keyContent = new String(
                    privateKeyResource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            // Limpiar el PEM: quitar headers y whitespace
            keyContent = keyContent
                    .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                    .replace("-----END RSA PRIVATE KEY-----", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] keyBytes = Base64.getDecoder().decode(keyContent);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            this.privateKey = (RSAPrivateKey) kf.generatePrivate(spec);


        } catch (Exception e) {

            throw new RuntimeException("No se pudo cargar la private key de JaaS", e);
        }
    }

    public String generateToken(String roomName, String userName,
                                String userEmail, boolean isModerator) {

        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(3 * 60 * 60); // 3 horas de validez

        // Claims del usuario
        Map<String, Object> userClaims = new HashMap<>();
        userClaims.put("name", userName);
        userClaims.put("email", userEmail != null ? userEmail : "");
        userClaims.put("moderator", isModerator);
        // Avatar opcional:
        // userClaims.put("avatar", "https://tu-app.com/avatars/user.png");

        // Features/permisos
        Map<String, Object> features = new HashMap<>();
        features.put("livestreaming", "false");
        features.put("recording", "false");
        features.put("transcription", "false");  // Usamos nuestra propia IA
        features.put("outbound-call", "false");
        features.put("sip-outbound-call", "false");
        features.put("inbound-call", "false");

        // Room claim
        Map<String, Object> roomClaim = new HashMap<>();
        roomClaim.put("regex", false);
        roomClaim.put("room", roomName);

        // Context
        Map<String, Object> context = new HashMap<>();
        context.put("user", userClaims);
        context.put("features", features);
        context.put("room", roomClaim);

        // Construir el JWT
        String jwt = Jwts.builder()
                .header()
                .keyId(keyId)
                .type("JWT")
                .and()
                .issuer("chat")
                .audience().add("jitsi").and()
                .subject(appId)
                .claim("context", context)
                .claim("room", "*")
                .issuedAt(Date.from(now))
                .notBefore(Date.from(now.minusSeconds(10)))
                .expiration(Date.from(expiry))
                .signWith(privateKey)
                .compact();

        log.info("[JaaS] JWT generado para usuario '{}' en sala '{}', moderator={}",
                userName, roomName, isModerator);

        return jwt;
    }

    public String getAppId() {
        return appId;
    }
}