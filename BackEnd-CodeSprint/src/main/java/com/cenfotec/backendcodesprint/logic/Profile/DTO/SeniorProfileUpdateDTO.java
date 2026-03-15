package com.cenfotec.backendcodesprint.logic.Profile.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SeniorProfileUpdateDTO {

    // ── Datos del User ────────────────────────────────────────────
    @NotBlank
    private String userName;

    @NotBlank
    private String lastName;

    @Email @NotBlank
    private String email;

    // ── Información personal ──────────────────────────────────────
    private Integer age;
    private String address;
    private String phone;
    private String profileImage;

    // ── Familiar responsable ──────────────────────────────────────
    private String familyMember;
    private String familyRelation;

    // ── Contacto de emergencia ────────────────────────────────────
    @NotBlank
    private String emergencyContactName;

    @NotBlank
    private String emergencyContactPhone;

    private String emergencyRelation;

    // ── Notas médicas ─────────────────────────────────────────────
    private String mobilityNotes;
    private String carePreference;
    private String healthObservation;
    private String allergies;
}