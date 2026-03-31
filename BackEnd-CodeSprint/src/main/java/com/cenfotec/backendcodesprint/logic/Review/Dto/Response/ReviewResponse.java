package com.cenfotec.backendcodesprint.logic.Review.Dto.Response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReviewResponse {
    private Long id;
    private Long providerProfileId;
    private Long serviceBookingId;
    private String author;
    private BigDecimal ranking;
    private String comment;
    private String createdAt;
}