package com.cenfotec.backendcodesprint.api.ServiceController;

import com.cenfotec.backendcodesprint.logic.Model.CareService;
import com.cenfotec.backendcodesprint.logic.Admin.Service.CareServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class ServiceController {

    private final CareServiceService careServiceService;

    // Obtener todos los servicios de un proveedor
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<CareService>> getServicesByProvider(@PathVariable Long providerId) {
        List<CareService> services = careServiceService.getServicesByProvider(providerId);
        return ResponseEntity.ok(services);
    }

    // Obtener un servicio por ID
    @GetMapping("/{id}")
    public ResponseEntity<CareService> getServiceById(@PathVariable Long id) {
        CareService service = careServiceService.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    // Crear un nuevo servicio
    @PostMapping
    public ResponseEntity<CareService> createService(@RequestBody CareService service) {
        CareService newService = careServiceService.createService(service);
        return ResponseEntity.ok(newService);
    }

    // Actualizar un servicio existente
    @PutMapping("/{id}")
    public ResponseEntity<CareService> updateService(@PathVariable Long id, @RequestBody CareService service) {
        CareService updatedService = careServiceService.updateService(id, service);
        return ResponseEntity.ok(updatedService);
    }

    // Cambiar estado (PAUSAR/ACTIVAR) - toggle
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<CareService> toggleStatus(@PathVariable Long id) {
        CareService service = careServiceService.toggleStatus(id);
        return ResponseEntity.ok(service);
    }

    // Cambiar estado específico (publicado/pausado)
    @PatchMapping("/{id}/status")
    public ResponseEntity<CareService> setStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newStatus = request.get("status");
        CareService service = careServiceService.setStatus(id, newStatus);
        return ResponseEntity.ok(service);
    }

    // Eliminar un servicio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        careServiceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener estadísticas del proveedor
    @GetMapping("/provider/{providerId}/stats")
    public ResponseEntity<Map<String, Long>> getStats(@PathVariable Long providerId) {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", careServiceService.countByProvider(providerId));
        stats.put("active", careServiceService.countActiveByProvider(providerId));
        stats.put("paused", careServiceService.countPausedByProvider(providerId));
        return ResponseEntity.ok(stats);
    }
}
