package com.cenfotec.backendcodesprint.logic.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "care_service")
public class CareService extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "care_service_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_profile_id", nullable = false)
    @NotNull
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ProviderProfile providerProfile;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_category_id", nullable = false)
    @NotNull
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ServiceCategory serviceCategory;

    @Column(name = "title", nullable = false, length = 150)
    @NotBlank
    private String title;

    @Column(name = "service_description", nullable = false, columnDefinition = "Text")
    @NotBlank
    private String serviceDescription;

    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal basePrice;

    @Column(name = "price_mode", nullable = false, length = 30)
    @NotBlank
    private String priceMode;

    @Column(name = "publication_state", nullable = false, length = 30)
    private String publicationState = "draft";

    @Column(name = "rejection_reason", columnDefinition = "Text")
    private String rejectionReason;
}

