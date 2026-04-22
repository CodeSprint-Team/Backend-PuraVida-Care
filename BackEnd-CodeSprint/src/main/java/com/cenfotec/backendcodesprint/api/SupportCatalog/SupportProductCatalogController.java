package com.cenfotec.backendcodesprint.api.SupportCatalog;


import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Dto.Request.SupportProductCatalogCreateRequest;
import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Dto.Request.SupportProductCatalogUpdateRequest;
import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Dto.Response.SupportProductCatalogResponse;
import com.cenfotec.backendcodesprint.logic.AdminSupportCatalog.Service.SupportProductCatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/support-product-catalogs")
@RequiredArgsConstructor
public class SupportProductCatalogController {

    private final SupportProductCatalogService service;

    @PostMapping
    public ResponseEntity<SupportProductCatalogResponse> create(
            @Valid @RequestBody SupportProductCatalogCreateRequest request) {
        SupportProductCatalogResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportProductCatalogResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SupportProductCatalogResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<SupportProductCatalogResponse>> getAllActive() {
        return ResponseEntity.ok(service.getAllActive());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupportProductCatalogResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SupportProductCatalogUpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
