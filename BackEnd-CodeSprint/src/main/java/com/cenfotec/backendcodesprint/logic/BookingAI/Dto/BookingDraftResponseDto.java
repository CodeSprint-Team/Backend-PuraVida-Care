package com.cenfotec.backendcodesprint.logic.BookingAI.Dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class BookingDraftResponseDto {

    private String draftId;
    private Long careServiceId;
    private String providerName;
    private String providerImage;
    private String serviceTitle;
    private String category;
    private String scheduledAt;
    private String originAddress;
    private String destinationAddress;
    private String zone;
    private String mobility;
    private String companion;
    private BigDecimal agreedPrice;
    private String agreedPriceMode;
    private String notes;
    private String seniorName;

    private boolean success;
    private String errorMessage;
    private String errorCode;
}
