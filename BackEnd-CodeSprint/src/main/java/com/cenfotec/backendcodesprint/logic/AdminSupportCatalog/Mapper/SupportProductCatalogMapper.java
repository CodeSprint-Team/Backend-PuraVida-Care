package com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Mapper;

import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Dto.Request.SupportProductCatalogCreateRequest;
import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Dto.Request.SupportProductCatalogUpdateRequest;
import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Dto.Response.SupportProductCatalogResponse;
import com.cenfotec.backendcodesprint.logic.Model.SupportProductCatalog;
import org.springframework.stereotype.Component;

@Component
public class SupportProductCatalogMapper {

    public SupportProductCatalog toEntity(SupportProductCatalogCreateRequest request) {
        SupportProductCatalog entity = new SupportProductCatalog();
        entity.setCategory(request.getCategory());
        entity.setBaseName(request.getBaseName());
        entity.setBaseDescription(request.getBaseDescription());
        entity.setActive(request.getActive() != null ? request.getActive() : true);
        return entity;
    }

    public void updateEntity(SupportProductCatalog entity, SupportProductCatalogUpdateRequest request) {
        entity.setCategory(request.getCategory());
        entity.setBaseName(request.getBaseName());
        entity.setBaseDescription(request.getBaseDescription());
        if (request.getActive() != null) {
            entity.setActive(request.getActive());
        }
    }

    public SupportProductCatalogResponse toResponse(SupportProductCatalog entity) {
        SupportProductCatalogResponse response = new SupportProductCatalogResponse();
        response.setId(entity.getId());
        response.setCategory(entity.getCategory());
        response.setBaseName(entity.getBaseName());
        response.setBaseDescription(entity.getBaseDescription());
        response.setActive(entity.getActive());
        return response;
    }
}
