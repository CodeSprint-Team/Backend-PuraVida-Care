package com.cenfotec.backendcodesprint.logic.Profile.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProviderProfileUpdateDTO {

    // ── Datos del User ────────────────────────────────────────────
    @NotBlank
    private String userName;

    @NotBlank
    private String lastName;

    @Email @NotBlank
    private String email;

    // ── Datos del perfil ──────────────────────────────────────────
    @NotBlank
    private String experienceDescription;

    private Integer experienceYears;
    private String providerState;
    private String bio;
    private String zone;
    private String phone;
    private String profileImage;
    private Boolean verified;
    private Boolean insuranceActive;
}