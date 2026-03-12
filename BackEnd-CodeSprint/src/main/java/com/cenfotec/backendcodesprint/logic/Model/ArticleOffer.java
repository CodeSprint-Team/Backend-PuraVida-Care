package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "article_offer")
public class ArticleOffer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_offer_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "support_product_post_id", nullable = false)
    @NotNull
    private SupportProductPost supportProductPost;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_user_id", nullable = false)
    @NotNull
    private User buyerUser;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    @Column(name = "offer_state", nullable = false, length = 20)
    private String offerState = "pending";

    @Column(name = "message", columnDefinition = "Text")
    private String message;
}
