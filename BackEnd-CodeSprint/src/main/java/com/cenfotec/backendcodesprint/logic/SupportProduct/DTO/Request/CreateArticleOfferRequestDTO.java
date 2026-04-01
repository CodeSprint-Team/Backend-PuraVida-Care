package com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateArticleOfferRequestDTO {
    private Long supportProductPostId;
    private Long buyerUserId;
    private BigDecimal amount;
    private String message;
}