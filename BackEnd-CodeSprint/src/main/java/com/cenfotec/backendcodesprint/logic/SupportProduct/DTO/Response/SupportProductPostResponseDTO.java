package com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class SupportProductPostResponseDTO {
    private Long id;

    private Long userId;
    private String userName;
    private String userLastName;
    private String userEmail;

    private Long supportProductCatalogId;
    private String supportProductCatalogName;

    private String title;
    private String description;
    private String condition;
    private BigDecimal salePrice;
    private BigDecimal originalPrice;
    private Boolean acceptsOffers;
    private String publicationState;

    private BigDecimal locationLat;
    private BigDecimal locationLng;
    private String locationText;
    private String usageTimeText;

    private LocalDateTime created;
    private LocalDateTime updated;

    private String imageUrl;
    private String imagePath;
}