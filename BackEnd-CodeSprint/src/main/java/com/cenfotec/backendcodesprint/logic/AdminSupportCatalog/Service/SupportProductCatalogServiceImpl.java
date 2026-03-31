package com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Service;


import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Dto.Request.SupportProductCatalogCreateRequest;
import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Dto.Request.SupportProductCatalogUpdateRequest;
import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Dto.Response.SupportProductCatalogResponse;
import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Mapper.SupportProductCatalogMapper;
import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Repository.SupportProductCatalogRepository;
import com.cenfotec.backendcodesprint.logic.Model.SupportProductCatalog;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupportProductCatalogServiceImpl implements SupportProductCatalogService {

    private final SupportProductCatalogRepository repository;
    private final SupportProductCatalogMapper mapper;

    @Override
    public SupportProductCatalogResponse create(SupportProductCatalogCreateRequest request) {
        SupportProductCatalog entity = mapper.toEntity(request);
        SupportProductCatalog saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SupportProductCatalogResponse getById(Long id) {
        SupportProductCatalog entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "SupportProductCatalog not found with id: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportProductCatalogResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportProductCatalogResponse> getAllActive() {
        return repository.findByActiveTrue()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SupportProductCatalogResponse update(Long id, SupportProductCatalogUpdateRequest request) {
        SupportProductCatalog entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "SupportProductCatalog not found with id: " + id));
        mapper.updateEntity(entity, request);
        SupportProductCatalog updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(
                    "SupportProductCatalog not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
