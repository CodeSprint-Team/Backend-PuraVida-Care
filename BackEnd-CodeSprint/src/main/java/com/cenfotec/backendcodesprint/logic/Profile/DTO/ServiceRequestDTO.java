package com.cenfotec.backendcodesprint.logic.Profile.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServiceRequestDTO {
    private String title;
    private String serviceDescription;
    private BigDecimal basePrice;
    private String priceMode;
    private Long providerProfileId;
    private Long serviceCategoryId;
    private String zone;
    private String modality;
    private RequirementsDTO requirements;

    @Data
    public static class RequirementsDTO {
        private Boolean experience;
        private Boolean license;
        private Boolean certification;
    }
}
