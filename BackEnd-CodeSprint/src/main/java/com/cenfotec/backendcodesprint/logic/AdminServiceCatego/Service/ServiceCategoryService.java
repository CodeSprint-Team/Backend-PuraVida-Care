package com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Service;



import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Dto.Request.ServiceCategoryCreateRequest;
import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Dto.Request.ServiceCategoryUpdateRequest;
import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Dto.Response.ServiceCategoryResponse;

import java.util.List;

public interface ServiceCategoryService {

    ServiceCategoryResponse create(ServiceCategoryCreateRequest request);

    ServiceCategoryResponse getById(Long id);

    List<ServiceCategoryResponse> getAll();

    List<ServiceCategoryResponse> getAllActive();

    ServiceCategoryResponse update(Long id, ServiceCategoryUpdateRequest request);

    void delete(Long id);
}
