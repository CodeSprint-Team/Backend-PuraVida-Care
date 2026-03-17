package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "client_profile")
public class ClientProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_profile_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @Column(name = "phone", nullable = false, length = 50)
    @NotBlank
    private String phone;

    @Column(name = "notes", columnDefinition = "Text")
    private String notes;

    @Column(name = "profile_image", columnDefinition = "Text")
    private String profileImage;

    @Column(name = "relation_to_senior", length = 100)
    private String relationToSenior;        // Relación con el adulto mayor

    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    @Column(name = "emergency_contact_relation", length = 100)
    private String emergencyContactRelation;

    @Column(name = "emergency_contact_phone", length = 50)
    private String emergencyContactPhone;

    @Column(name = "important_notes", columnDefinition = "Text")
    private String importantNotes;
}