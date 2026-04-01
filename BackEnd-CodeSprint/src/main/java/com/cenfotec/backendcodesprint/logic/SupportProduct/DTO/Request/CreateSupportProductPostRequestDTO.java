package com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateSupportProductPostRequestDTO {

    private Long userId;

    private Long supportProductCatalogId;

    private String title;

    private String description;

    private String condition;

    private BigDecimal salePrice;

    private BigDecimal originalPrice;

    private Boolean acceptsOffers;

    private BigDecimal locationLat;

    private BigDecimal locationLng;

    private String locationText;

    private String usageTimeText;
}