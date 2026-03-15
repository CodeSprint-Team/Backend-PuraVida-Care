package com.cenfotec.backendcodesprint.logic.Profile.DTO;

import lombok.Data;
import java.util.List;

@Data
public class SeniorProfileResponseDTO {

    private Long id;

    // ── Datos del User ────────────────────────────────────────────
    private String fullName;
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
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyRelation;

    // ── Notas médicas ─────────────────────────────────────────────
    private String mobilityNotes;
    private String carePreference;
    private String healthObservation;
    private String allergies;

    // ── Proveedores favoritos ─────────────────────────────────────
    private List<FavoriteProviderDTO> favoriteProviders;

    @Data
    public static class FavoriteProviderDTO {
        private Long favoriteId;
        private Long providerProfileId;
        private String fullName;
        private String providerType;
        private Double averageRating;
        private String providerState;
    }
}