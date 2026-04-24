package com.cenfotec.backendcodesprint.api.CreateController;

import com.cenfotec.backendcodesprint.logic.CreateService.DTO.CareServiceRequestDTO;
import com.cenfotec.backendcodesprint.logic.CreateService.DTO.CareServiceResponseDTO;
import com.cenfotec.backendcodesprint.logic.CreateService.DTO.ServiceStatsDTO;
import com.cenfotec.backendcodesprint.logic.CreateService.Service.CareServiceManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/services")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CareServiceController {

    private final CareServiceManagementService serviceManagement;

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<CareServiceResponseDTO>> getByProvider(
            @PathVariable Long providerId) {
        return ResponseEntity.ok(serviceManagement.getByProvider(providerId));
    }

    @GetMapping("/provider/{providerId}/stats")
    public ResponseEntity<ServiceStatsDTO> getStats(@PathVariable Long providerId) {
        return ResponseEntity.ok(serviceManagement.getStats(providerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CareServiceResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceManagement.getById(id));
    }

    @PostMapping
    public ResponseEntity<CareServiceResponseDTO> create(
            @RequestBody CareServiceRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceManagement.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CareServiceResponseDTO> update(
            @PathVariable Long id,
            @RequestBody CareServiceRequestDTO dto) {
        return ResponseEntity.ok(serviceManagement.update(id, dto));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<CareServiceResponseDTO> toggle(@PathVariable Long id) {
        return ResponseEntity.ok(serviceManagement.toggleStatus(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CareServiceResponseDTO> setStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(serviceManagement.setStatus(id, body.get("status")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        serviceManagement.delete(id);
        return ResponseEntity.noContent().build();
    }
}