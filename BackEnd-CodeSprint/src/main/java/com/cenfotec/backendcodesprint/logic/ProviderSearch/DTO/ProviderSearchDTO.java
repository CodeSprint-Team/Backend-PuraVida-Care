package com.cenfotec.backendcodesprint.logic.ProviderSearch.DTO;

import lombok.Data;

@Data
public class ProviderSearchDTO {
    private String  name;
    private String  zone;
    private Double  minPrice;
    private Double  maxPrice;
    private Double  minRating;
    private String  category;
    private Boolean verifiedOnly;
    private Boolean availableToday;
}