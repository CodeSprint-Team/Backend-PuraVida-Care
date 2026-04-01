package com.cenfotec.backendcodesprint.api.ServiceCategoController;


import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Dto.Request.ServiceCategoryCreateRequest;
import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Dto.Request.ServiceCategoryUpdateRequest;
import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Dto.Response.ServiceCategoryResponse;
import com.cenfotec.backendcodesprint.logic.AdminServiceCatego.Service.ServiceCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-categories")
@RequiredArgsConstructor
public class ServiceCategoryController {

    private final ServiceCategoryService service;

    @PostMapping
    public ResponseEntity<ServiceCategoryResponse> create(
            @Valid @RequestBody ServiceCategoryCreateRequest request) {
        ServiceCategoryResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceCategoryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ServiceCategoryResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<ServiceCategoryResponse>> getAllActive() {
        return ResponseEntity.ok(service.getAllActive());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceCategoryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ServiceCategoryUpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}