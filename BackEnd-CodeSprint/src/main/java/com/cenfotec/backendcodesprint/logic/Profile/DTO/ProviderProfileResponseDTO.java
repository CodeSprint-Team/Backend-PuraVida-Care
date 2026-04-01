package com.cenfotec.backendcodesprint.logic.Profile.DTO;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ProviderProfileResponseDTO {

    private Long id;

    // ── Datos del User ────────────────────────────────────────────
    private String fullName;
    private String email;

    // ── Datos del perfil ──────────────────────────────────────────
    private String providerType;
    private String experienceDescription;
    private Integer experienceYears;
    private BigDecimal averageRating;
    private String providerState;
    private String bio;
    private String zone;
    private String phone;
    private String profileImage;
    private Boolean verified;
    private Boolean insuranceActive;

    private List<CareServiceDTO> services;

    private Integer totalReviews;
    private List<ReviewDTO> reviewsList;
    private Map<Integer, Double> ratingDistribution;

    @Data
    public static class CareServiceDTO {
        private Long id;
        private String name;            // title del CareService
        private String description;     // serviceDescription
        private String price;           // basePrice formateado (₡ 15,000)
        private String priceMode;       // por hora, por visita, etc.
        private String category;        // serviceCategory name
        private String publicationState;
    }

    @Data
    public static class ReviewDTO {
        private Long id;
        private String author;          // fullName del cliente/senior
        private String avatar;          // null por ahora, se puede agregar después
        private Double rating;
        private String comment;
        private String date;            // createdAt formateado
    }
}