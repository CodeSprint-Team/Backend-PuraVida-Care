package com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Mapper;

import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Dto.Request.ServiceCategoryCreateRequest;
import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Dto.Request.ServiceCategoryUpdateRequest;
import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Dto.Response.ServiceCategoryResponse;
import com.cenfotec.backendcodesprint.logic.Model.ServiceCategory;
import org.springframework.stereotype.Component;

@Component
public class ServiceCategoryMapper {

    public ServiceCategory toEntity(ServiceCategoryCreateRequest request) {
        ServiceCategory entity = new ServiceCategory();
        entity.setCategoryName(request.getCategoryName());
        entity.setCategoryState(request.getCategoryState() != null ? request.getCategoryState() : "active");
        return entity;
    }

    public void updateEntity(ServiceCategory entity, ServiceCategoryUpdateRequest request) {
        entity.setCategoryName(request.getCategoryName());
        if (request.getCategoryState() != null) {
            entity.setCategoryState(request.getCategoryState());
        }
    }

    public ServiceCategoryResponse toResponse(ServiceCategory entity) {
        ServiceCategoryResponse response = new ServiceCategoryResponse();
        response.setId(entity.getId());
        response.setCategoryName(entity.getCategoryName());
        response.setCategoryState(entity.getCategoryState());
        return response;
    }
}