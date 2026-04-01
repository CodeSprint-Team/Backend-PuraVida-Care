package com.cenfotec.backendcodesprint.logic.ServiceCategory;

import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Dto.Request.ServiceCategoryCreateRequest;
import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Dto.Request.ServiceCategoryUpdateRequest;
import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Dto.Response.ServiceCategoryResponse;
import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Mapper.ServiceCategoryMapper;
import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Repository.ServiceCategoryRepository;
import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Service.ServiceCategoryServiceImpl;
import com.cenfotec.backendcodesprint.logic.Model.ServiceCategory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceCategoryServiceImplTest {

    @Mock
    private ServiceCategoryRepository repository;

    @Mock
    private ServiceCategoryMapper mapper;

    @InjectMocks
    private ServiceCategoryServiceImpl service;

    private ServiceCategory entity;
    private ServiceCategoryResponse response;

    @BeforeEach
    void setUp() {
        entity = new ServiceCategory();
        entity.setId(1L);
        entity.setCategoryState("active");

        response = new ServiceCategoryResponse();
    }

    @Test
    void create_ShouldCreateSuccessfully() {

        ServiceCategoryCreateRequest request = new ServiceCategoryCreateRequest();

        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        ServiceCategoryResponse result = service.create(request);

        assertNotNull(result);
        verify(repository, times(1)).save(entity);
    }

    @Test
    void getById_ShouldReturnEntity() {

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        ServiceCategoryResponse result = service.getById(1L);

        assertNotNull(result);
    }

    @Test
    void getById_WhenNotFound_ShouldThrowException() {

        when(repository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            service.getById(99L);
        });

        assertTrue(exception.getMessage().contains("ServiceCategory not found"));
    }

    @Test
    void getAll_ShouldReturnList() {

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<ServiceCategoryResponse> result = service.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getAllActive_ShouldReturnOnlyActiveList() {

        when(repository.findByCategoryState("active")).thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<ServiceCategoryResponse> result = service.getAllActive();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void update_ShouldUpdateSuccessfully() {

        ServiceCategoryUpdateRequest request = new ServiceCategoryUpdateRequest();

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        ServiceCategoryResponse result = service.update(1L, request);

        assertNotNull(result);
        verify(mapper, times(1)).updateEntity(entity, request);
        verify(repository, times(1)).save(entity);
    }

    @Test
    void update_WhenNotFound_ShouldThrowException() {

        ServiceCategoryUpdateRequest request = new ServiceCategoryUpdateRequest();

        when(repository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            service.update(99L, request);
        });

        assertTrue(exception.getMessage().contains("ServiceCategory not found"));
    }

    @Test
    void delete_ShouldDeleteSuccessfully() {

        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void delete_WhenNotFound_ShouldThrowException() {

        when(repository.existsById(99L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            service.delete(99L);
        });

        assertTrue(exception.getMessage().contains("ServiceCategory not found"));
    }
}

