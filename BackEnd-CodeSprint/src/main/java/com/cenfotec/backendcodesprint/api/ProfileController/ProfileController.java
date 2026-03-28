package com.cenfotec.backendcodesprint.api.ProfileController;

import com.cenfotec.backendcodesprint.logic.Profile.DTO.*;
import com.cenfotec.backendcodesprint.logic.Profile.Service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/profiles")
@CrossOrigin(origins = "")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    // ═══════════════════════════════════════════════════════════════
    // SENIOR
    // ═══════════════════════════════════════════════════════════════

    @GetMapping("/senior/{id}")
    public ResponseEntity<SeniorProfileResponseDTO> getSeniorProfile(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getSeniorProfile(id));
    }

    @GetMapping("/senior/by-user/{userId}")
    public ResponseEntity<SeniorProfileResponseDTO> getSeniorProfileByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getSeniorProfileByUserId(userId));
    }

    @PutMapping("/senior/{id}")
    public ResponseEntity<SeniorProfileResponseDTO> updateSeniorProfile(
            @PathVariable Long id,
            @Valid @RequestBody SeniorProfileUpdateDTO dto) {
        return ResponseEntity.ok(profileService.updateSeniorProfile(id, dto));
    }

    @PostMapping("/senior/{seniorId}/favorites/{providerProfileId}")
    public ResponseEntity<SeniorProfileResponseDTO> addFavorite(
            @PathVariable Long seniorId,
            @PathVariable Long providerProfileId) {
        return ResponseEntity.ok(profileService.addFavoriteProvider(seniorId, providerProfileId));
    }

    @DeleteMapping("/senior/{seniorId}/favorites/{providerProfileId}")
    public ResponseEntity<SeniorProfileResponseDTO> removeFavorite(
            @PathVariable Long seniorId,
            @PathVariable Long providerProfileId) {
        return ResponseEntity.ok(profileService.removeFavoriteProvider(seniorId, providerProfileId));
    }

    @PostMapping("/senior")
    public ResponseEntity<SeniorProfileResponseDTO> createSeniorProfile(
            @Valid @RequestBody SeniorProfileCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileService.createSeniorProfile(dto));
    }

    // ═══════════════════════════════════════════════════════════════
    // CLIENT
    // ═══════════════════════════════════════════════════════════════

    @GetMapping("/client/{id}")
    public ResponseEntity<ClientProfileResponseDTO> getClientProfile(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getClientProfile(id));
    }

    @GetMapping("/client/by-user/{userId}")
    public ResponseEntity<?> getClientProfileByUserId(@PathVariable Long userId) {
        Optional<ClientProfileResponseDTO> profile =
                profileService.getClientProfileByUserIdOptional(userId);

        if (profile.isPresent()) {
            return ResponseEntity.ok(profile.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", "Client profile not found",
                    "profileCompleted", false
            ));
        }
    }

    @PutMapping("/client/{id}")
    public ResponseEntity<ClientProfileResponseDTO> updateClientProfile(
            @PathVariable Long id,
            @Valid @RequestBody ClientProfileUpdateDTO dto) {
        return ResponseEntity.ok(profileService.updateClientProfile(id, dto));
    }

    @PostMapping("/client")
    public ResponseEntity<ClientProfileResponseDTO> createClientProfile(
            @Valid @RequestBody ClientProfileCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileService.createClientProfile(dto));
    }

    @GetMapping("/client/by-email/{email}")
    public ResponseEntity<ClientProfileResponseDTO> getClientProfileByEmail(@PathVariable String email) {
        return ResponseEntity.ok(profileService.getClientProfileByEmail(email));
    }

    // ═══════════════════════════════════════════════════════════════
    // PROVIDER
    // ═══════════════════════════════════════════════════════════════

    @GetMapping("/provider/{id}")
    public ResponseEntity<ProviderProfileResponseDTO> getProviderProfile(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getProviderProfile(id));
    }

    @GetMapping("/provider/by-user/{userId}")
    public ResponseEntity<ProviderProfileResponseDTO> getProviderProfileByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getProviderProfileByUserId(userId));
    }

    @PutMapping("/provider/{id}")
    public ResponseEntity<ProviderProfileResponseDTO> updateProviderProfile(
            @PathVariable Long id,
            @Valid @RequestBody ProviderProfileUpdateDTO dto) {
        return ResponseEntity.ok(profileService.updateProviderProfile(id, dto));
    }

    @PostMapping("/provider")
    public ResponseEntity<ProviderProfileResponseDTO> createProviderProfile(
            @Valid @RequestBody ProviderProfileCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileService.createProviderProfile(dto));
    }

    // ═══════════════════════════════════════════════════════════════
    // ADMIN
    // ═══════════════════════════════════════════════════════════════

    @GetMapping("/admin/by-user/{userId}")
    public ResponseEntity<AdminProfileResponseDTO> getAdminProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getAdminProfileByUserId(userId));
    }

    @PutMapping("/admin/by-user/{userId}")
    public ResponseEntity<AdminProfileResponseDTO> updateAdminProfile(
            @PathVariable Long userId,
            @Valid @RequestBody AdminProfileUpdateDTO dto) {
        return ResponseEntity.ok(profileService.updateAdminProfile(userId, dto));
    }
}