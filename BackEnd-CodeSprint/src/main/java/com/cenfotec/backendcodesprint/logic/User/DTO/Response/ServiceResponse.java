package com.cenfotec.backendcodesprint.logic.User.DTO.Response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServiceResponse {
    private Long id;
    private String title;
    private String category;
    private String description;
    private BigDecimal price;
    private String priceUnit;
    private String status;
    private BigDecimal rating;
    private Integer reviews;
    private String createdAt;
}
