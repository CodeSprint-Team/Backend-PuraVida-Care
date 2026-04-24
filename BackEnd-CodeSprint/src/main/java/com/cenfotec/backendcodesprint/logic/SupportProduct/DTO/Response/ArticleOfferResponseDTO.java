package com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Response;

import com.cenfotec.backendcodesprint.logic.Model.OfferStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleOfferResponseDTO {

    private Long id;
    private Long supportProductPostId;

    private String supportProductTitle;
    private String supportProductImageUrl;

    private Long sellerUserId;
    private String sellerName;

    private Long buyerUserId;
    private String buyerUserName;

    private BigDecimal amount;
    private String message;
    private OfferStatus offerState;

    private LocalDateTime created;
    private LocalDateTime updated;

    private String publicationState;
}