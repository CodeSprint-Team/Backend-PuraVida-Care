package com.cenfotec.backendcodesprint.logic.CreateService.DTO;

import lombok.Data;

@Data
public class CareServiceRequestDTO {
    private String title;
    private String serviceDescription;
    private Double basePrice;
    private String priceMode;
    private ProviderRef providerProfile;
    private CategoryRef serviceCategory;
    private String zone;
    private String modality;

    @Data
    public static class ProviderRef {
        private Long id;
    }

    @Data
    public static class CategoryRef {
        private Long id;
    }
}
