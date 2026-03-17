package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@Entity
@Table(name = "provider_profile")
public class ProviderProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provider_profile_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_type_id", nullable = false)
    @NotNull
    private ProviderType providerType;

    @Column(name = "experience_description", nullable = false, columnDefinition = "Text")
    @NotBlank
    private String experienceDescription;

    @Column(name = "experience_years", nullable = false)
    @NotNull
    private Integer experienceYears = 0;

    @Column(name = "average_rating", nullable = false, precision = 3, scale = 2)
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "provider_state", nullable = false, length = 20)
    private String providerState = "pending";

    @Column(name = "bio", columnDefinition = "Text")
    private String bio;

    @Column(name = "zone", length = 150)
    private String zone;

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "profile_image", columnDefinition = "Text")
    private String profileImage;

    @Column(name = "verified", nullable = false)
    private Boolean verified = false;

    @Column(name = "insurance_active", nullable = false)
    private Boolean insuranceActive = false;
}