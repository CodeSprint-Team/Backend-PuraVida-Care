package com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Dto.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportProductCatalogResponse {

    private Long id;
    private String category;
    private String baseName;
    private String baseDescription;
    private Boolean active;
}
