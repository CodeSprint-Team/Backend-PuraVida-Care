package com.cenfotec.backendcodesprint.logic.ProviderSearch.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ProviderSearchResultDTO {
    private Long id;
    private String fullName;
    private String profileImage;
    private String providerType;
    private String zone;
    private Double averageRating;
    private Integer totalReviews;
    private Boolean verified;
    private Boolean insuranceActive;
    private String bio;
    private String providerState;
    private Double startingPrice;
    private String startingPriceMode;
    private List<String> serviceCategories;
}
