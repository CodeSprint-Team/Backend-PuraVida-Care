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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @Column(name = "emergency_contact_name", nullable = false, length = 100)
    @NotBlank
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", nullable = false, length = 50)
    @NotBlank
    private String emergencyContactPhone;

    @Column(name = "mobility_notes", columnDefinition = "Text")
    private String mobilityNotes;

    @Column(name = "care_preference", columnDefinition = "Text")
    private String carePreference;

    @Column(name = "health_observation", columnDefinition = "Text")
    private String healthObservation;
}
