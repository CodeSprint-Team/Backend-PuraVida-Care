package com.cenfotec.backendcodesprint.logic.CreateService.DTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CareServiceResponseDTO {
    private Long   id;
    private String title;
    private String serviceDescription;
    private BigDecimal basePrice;
    private String priceMode;
    private String publicationState;
    private String zone;
    private String modality;

    private ProviderRef   providerProfile;
    private CategoryRef   serviceCategory;

    private LocalDateTime created;
    private LocalDateTime updated;

    @Data
    public static class ProviderRef {
        private Long   id;
        private String fullName;
    }

    @Data
    public static class CategoryRef {
        private Long   id;
        private String categoryName;
    }
}