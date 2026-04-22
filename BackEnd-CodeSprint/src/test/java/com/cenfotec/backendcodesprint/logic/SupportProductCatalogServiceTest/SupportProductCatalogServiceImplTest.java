package com.cenfotec.backendcodesprint.logic.SupportProductCatalogServiceTest;

import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Dto.Request.SupportProductCatalogCreateRequest;
import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Dto.Request.SupportProductCatalogUpdateRequest;
import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Dto.Response.SupportProductCatalogResponse;
import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Mapper.SupportProductCatalogMapper;
import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Repository.SupportProductCatalogRepository;
import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Service.SupportProductCatalogServiceImpl;
import com.cenfotec.backendcodesprint.logic.Model.SupportProductCatalog;
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
class SupportProductCatalogServiceImplTest {

    @Mock
    private SupportProductCatalogRepository repository;

    @Mock
    private SupportProductCatalogMapper mapper;

    @InjectMocks
    private SupportProductCatalogServiceImpl service;

    private SupportProductCatalog entity;
    private SupportProductCatalogResponse response;

    @BeforeEach
    void setUp() {
        entity = new SupportProductCatalog();
        entity.setId(1L);
        entity.setActive(true);

        response = new SupportProductCatalogResponse();
    }

    @Test
    void create_ShouldCreateSuccessfully() {

        SupportProductCatalogCreateRequest request = new SupportProductCatalogCreateRequest();

        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        SupportProductCatalogResponse result = service.create(request);

        assertNotNull(result);
        verify(repository, times(1)).save(entity);
    }

    @Test
    void getById_ShouldReturnEntity() {

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        SupportProductCatalogResponse result = service.getById(1L);

        assertNotNull(result);
    }

    @Test
    void getById_WhenNotFound_ShouldThrowException() {

        when(repository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            service.getById(99L);
        });

        assertTrue(exception.getMessage().contains("SupportProductCatalog not found"));
    }

    @Test
    void getAll_ShouldReturnList() {

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<SupportProductCatalogResponse> result = service.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getAllActive_ShouldReturnOnlyActiveList() {

        when(repository.findByActiveTrue()).thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<SupportProductCatalogResponse> result = service.getAllActive();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void update_ShouldUpdateSuccessfully() {

        SupportProductCatalogUpdateRequest request = new SupportProductCatalogUpdateRequest();

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        SupportProductCatalogResponse result = service.update(1L, request);

        assertNotNull(result);
        verify(mapper, times(1)).updateEntity(entity, request);
        verify(repository, times(1)).save(entity);
    }

    @Test
    void update_WhenNotFound_ShouldThrowException() {

        SupportProductCatalogUpdateRequest request = new SupportProductCatalogUpdateRequest();

        when(repository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            service.update(99L, request);
        });

        assertTrue(exception.getMessage().contains("SupportProductCatalog not found"));
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

        assertTrue(exception.getMessage().contains("SupportProductCatalog not found"));
    }
}


