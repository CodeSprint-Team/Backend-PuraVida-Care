package com.cenfotec.backendcodesprint.api.ServiceController;

import com.cenfotec.backendcodesprint.logic.Model.CareService;
import com.cenfotec.backendcodesprint.logic.Admin.Service.CareServiceService;
import com.cenfotec.backendcodesprint.logic.Profile.DTO.ServiceRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/services")  //
@CrossOrigin(origins = "http://localhost:4200")  //
@RequiredArgsConstructor
public class ServiceController {

    private final CareServiceService careServiceService;

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<CareService>> getServicesByProvider(@PathVariable Long providerId) {
        List<CareService> services = careServiceService.getServicesByProvider(providerId);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CareService> getServiceById(@PathVariable Long id) {
        CareService service = careServiceService.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @PostMapping
    public ResponseEntity<CareService> createService(@RequestBody ServiceRequestDTO request) {
        CareService newService = careServiceService.createService(request);
        return ResponseEntity.ok(newService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CareService> updateService(@PathVariable Long id, @RequestBody CareService service) {
        CareService updatedService = careServiceService.updateService(id, service);
        return ResponseEntity.ok(updatedService);
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<CareService> toggleStatus(@PathVariable Long id) {
        CareService service = careServiceService.toggleStatus(id);
        return ResponseEntity.ok(service);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CareService> setStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newStatus = request.get("status");
        CareService service = careServiceService.setStatus(id, newStatus);
        return ResponseEntity.ok(service);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        careServiceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/provider/{providerId}/stats")
    public ResponseEntity<Map<String, Long>> getStats(@PathVariable Long providerId) {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", careServiceService.countByProvider(providerId));
        stats.put("active", careServiceService.countActiveByProvider(providerId));
        stats.put("paused", careServiceService.countPausedByProvider(providerId));
        return ResponseEntity.ok(stats);
    }
}