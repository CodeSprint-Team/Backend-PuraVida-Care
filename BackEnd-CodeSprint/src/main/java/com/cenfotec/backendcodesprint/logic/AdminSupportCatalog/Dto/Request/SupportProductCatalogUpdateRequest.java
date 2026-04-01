package com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportProductCatalogUpdateRequest {

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category must not exceed 50 characters")
    private String category;

    @NotBlank(message = "Base name is required")
    @Size(max = 100, message = "Base name must not exceed 100 characters")
    private String baseName;

    private String baseDescription;

    private Boolean active;
}
