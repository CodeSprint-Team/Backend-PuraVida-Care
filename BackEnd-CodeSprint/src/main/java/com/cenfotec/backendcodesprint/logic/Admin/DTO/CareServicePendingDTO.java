package com.cenfotec.backendcodesprint.logic.Admin.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class CareServicePendingDTO {
    private Long careServiceId;
    private Long providerProfileId;
    private String providerName;
    private String providerEmail;
    private String title;
    private String serviceDescription;
    private BigDecimal basePrice;
    private String priceMode;
    private String serviceCategory;
    private String publicationState;
    private String rejectionReason;
}
