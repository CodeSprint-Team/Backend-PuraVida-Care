package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_profile_id")
    private SeniorProfile seniorProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_profile_id")
    private ClientProfile clientProfile;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_profile_id", nullable = false)
    @NotNull
    private ProviderProfile providerProfile;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_booking_id", nullable = false)
    @NotNull
    private ServiceBooking serviceBooking;

    @Column(name = "ranking", nullable = false, precision = 2, scale = 1)
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "5.0", inclusive = true)
    private BigDecimal ranking;

    @Column(name = "comment", columnDefinition = "Text")
    private String comment;

    @Column(name = "total_review")
    private Integer totalReview;
}

