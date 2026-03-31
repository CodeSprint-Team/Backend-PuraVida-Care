package com.cenfotec.backendcodesprint.logic.Review.Dto.Request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateReview {

    @NotNull
    private Long serviceBookingId;

    private Long clientProfileId;

    private Long seniorProfileId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "5.0", inclusive = true)
    private BigDecimal ranking;

    private String comment;
}