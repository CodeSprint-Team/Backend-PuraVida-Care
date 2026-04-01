package com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Service;



import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Dto.Request.SupportProductCatalogCreateRequest;
import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Dto.Request.SupportProductCatalogUpdateRequest;
import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Dto.Response.SupportProductCatalogResponse;

import java.util.List;

public interface SupportProductCatalogService {

    SupportProductCatalogResponse create(SupportProductCatalogCreateRequest request);

    SupportProductCatalogResponse getById(Long id);

    List<SupportProductCatalogResponse> getAll();

    List<SupportProductCatalogResponse> getAllActive();

    SupportProductCatalogResponse update(Long id, SupportProductCatalogUpdateRequest request);

    void delete(Long id);
}
