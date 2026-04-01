package com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Service;

import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Dto.Request.ServiceCategoryCreateRequest;
import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Dto.Request.ServiceCategoryUpdateRequest;
import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Dto.Response.ServiceCategoryResponse;
import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Mapper.ServiceCategoryMapper;
import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Repository.ServiceCategoryRepository;
import com.cenfotec.backendcodesprint.logic.Model.ServiceCategory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceCategoryServiceImpl implements ServiceCategoryService {

    private final ServiceCategoryRepository repository;
    private final ServiceCategoryMapper mapper;

    @Override
    public ServiceCategoryResponse create(ServiceCategoryCreateRequest request) {
        ServiceCategory entity = mapper.toEntity(request);
        ServiceCategory saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceCategoryResponse getById(Long id) {
        ServiceCategory entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "ServiceCategory not found with id: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceCategoryResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceCategoryResponse> getAllActive() {
        return repository.findByCategoryState("active")
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceCategoryResponse update(Long id, ServiceCategoryUpdateRequest request) {
        ServiceCategory entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "ServiceCategory not found with id: " + id));
        mapper.updateEntity(entity, request);
        ServiceCategory updated = repository.save(entity);
        return mapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(
                    "ServiceCategory not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
