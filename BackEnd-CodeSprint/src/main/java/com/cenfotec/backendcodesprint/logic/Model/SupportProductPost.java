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
@Table(name = "support_product_post")
public class SupportProductPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "support_product_post_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "support_product_catalog_id", nullable = false)
    @NotNull
    private SupportProductCatalog supportProductCatalog;

    @Column(name = "title", nullable = false, length = 150)
    @NotBlank
    private String title;

    @Column(name = "article_description", nullable = false,columnDefinition = "Text")
    @NotBlank
    private String articleDescription;

    @Column(name = "condition_state", nullable = false, length = 20)
    @NotBlank
    private String conditionState;

    @Column(name = "original_price", nullable = false, precision = 12, scale = 2)
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal originalPrice;

    @Column(name = "accept_offers", nullable = false)
    private Boolean acceptOffers = true;

    @Column(name = "publication_state", nullable = false, length = 20)
    private String publicationState = "active";

    @Column(name = "location_longitude", nullable = false, precision = 9, scale = 6)
    @NotNull
    private BigDecimal locationLongitude;

    @Column(name = "location_latitude", nullable = false, precision = 9, scale = 6)
    @NotNull
    private BigDecimal locationLatitude;

    @Column(name = "location_text",columnDefinition = "Text")
    private String locationText;

    @Column(name = "sale_price", nullable = false, precision = 12, scale = 2)
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal salePrice;

    @Column(name = "usage_time_text", length = 50)
    private String usageTimeText;
}
