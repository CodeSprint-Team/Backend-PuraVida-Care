package com.cenfotec.backendcodesprint.api.ProfileController;

import com.cenfotec.backendcodesprint.logic.Profile.DTO.*;
import com.cenfotec.backendcodesprint.logic.Profile.Service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
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

    @PutMapping("/senior/{id}")
    public ResponseEntity<SeniorProfileResponseDTO> updateSeniorProfile(
            @PathVariable Long id,
            @Valid @RequestBody SeniorProfileUpdateDTO dto) {
        return ResponseEntity.ok(profileService.updateSeniorProfile(id, dto));
    }

    // ── Favoritos ─────────────────────────────────────────────────

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

    //post
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

    @PutMapping("/client/{id}")
    public ResponseEntity<ClientProfileResponseDTO> updateClientProfile(
            @PathVariable Long id,
            @Valid @RequestBody ClientProfileUpdateDTO dto) {
        return ResponseEntity.ok(profileService.updateClientProfile(id, dto));
    }

    //post
    @PostMapping("/client")
    public ResponseEntity<ClientProfileResponseDTO> createClientProfile(
            @Valid @RequestBody ClientProfileCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileService.createClientProfile(dto));
    }


    // ═══════════════════════════════════════════════════════════════
    // PROVIDER
    // ═══════════════════════════════════════════════════════════════

    @GetMapping("/provider/{id}")
    public ResponseEntity<ProviderProfileResponseDTO> getProviderProfile(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getProviderProfile(id));
    }

    @PutMapping("/provider/{id}")
    public ResponseEntity<ProviderProfileResponseDTO> updateProviderProfile(
            @PathVariable Long id,
            @Valid @RequestBody ProviderProfileUpdateDTO dto) {
        return ResponseEntity.ok(profileService.updateProviderProfile(id, dto));
    }

    //post
    @PostMapping("/provider")
    public ResponseEntity<ProviderProfileResponseDTO> createProviderProfile(
            @Valid @RequestBody ProviderProfileCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileService.createProviderProfile(dto));
    }
}