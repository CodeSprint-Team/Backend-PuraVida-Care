package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "favorite_provider")
public class FavoriteProvider extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_provider_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_profile_id", nullable = true)   // ✅ explícito
    private ClientProfile clientProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_profile_id", nullable = true)   // ✅ explícito
    private SeniorProfile seniorProfile;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_profile_id", nullable = false)
    @NotNull
    private ProviderProfile providerProfile;
}