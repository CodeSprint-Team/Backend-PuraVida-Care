package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "senior_profile")
public class SeniorProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "senior_profile_id")
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    // ── Información personal ──────────────────────────────────────
    @Column(name = "age")
    private Integer age;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "profile_image", columnDefinition = "Text")
    private String profileImage;

    // ── Familiar responsable ──────────────────────────────────────
    @Column(name = "family_member", length = 150)
    private String familyMember;         // Nombre del familiar

    @Column(name = "family_relation", length = 100)
    private String familyRelation;       // Parentesco (hijo, hija, etc.)

    // ── Contacto de emergencia ────────────────────────────────────
    @Column(name = "emergency_contact_name", nullable = false, length = 100)
    @NotBlank
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", nullable = false, length = 50)
    @NotBlank
    private String emergencyContactPhone;

    @Column(name = "emergency_relation", length = 100)
    private String emergencyRelation;

    // ── Notas médicas ─────────────────────────────────────────────
    @Column(name = "mobility_notes", columnDefinition = "Text")
    private String mobilityNotes;

    @Column(name = "care_preference", columnDefinition = "Text")
    private String carePreference;

    @Column(name = "health_observation", columnDefinition = "Text")
    private String healthObservation;

    @Column(name = "allergies", columnDefinition = "Text")
    private String allergies;
}