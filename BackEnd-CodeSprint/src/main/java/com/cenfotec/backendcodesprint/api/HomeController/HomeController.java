package com.cenfotec.backendcodesprint.api.HomeController;

import com.cenfotec.backendcodesprint.logic.Model.Home;
import com.cenfotec.backendcodesprint.logic.Model.HomeMarker;
import com.cenfotec.backendcodesprint.logic.User.DTO.Home.HomeMarkerDTO;
import com.cenfotec.backendcodesprint.logic.User.Service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/home")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    // ========== HOME ENDPOINTS ==========

    @GetMapping("/elderly/{elderlyId}")
    public ResponseEntity<Home> getHomeByElderlyId(@PathVariable Long elderlyId) {
        Home home = homeService.getHomeByElderlyId(elderlyId);
        if (home == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(home);
    }

    @PostMapping
    public ResponseEntity<Home> createHome(@RequestBody Home home) {
        return ResponseEntity.ok(homeService.createHome(home));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Home> updateHome(@PathVariable Long id, @RequestBody Home home) {
        return ResponseEntity.ok(homeService.updateHome(id, home));
    }

    // ========== MARKER ENDPOINTS ==========

    @GetMapping("/{homeId}/markers")
    public ResponseEntity<List<HomeMarker>> getMarkers(@PathVariable Long homeId) {
        return ResponseEntity.ok(homeService.getMarkersByHomeId(homeId));
    }

    @GetMapping("/{homeId}/markers/active")
    public ResponseEntity<List<HomeMarker>> getActiveMarkers(@PathVariable Long homeId) {
        return ResponseEntity.ok(homeService.getActiveMarkersByHomeId(homeId));
    }

    @GetMapping("/{homeId}/markers/type/{type}")
    public ResponseEntity<List<HomeMarker>> getMarkersByType(@PathVariable Long homeId, @PathVariable String type) {
        return ResponseEntity.ok(homeService.getMarkersByHomeIdAndType(homeId, type));
    }

    @GetMapping("/{homeId}/markers/room/{room}")
    public ResponseEntity<List<HomeMarker>> getMarkersByRoom(@PathVariable Long homeId, @PathVariable String room) {
        return ResponseEntity.ok(homeService.getMarkersByHomeIdAndRoom(homeId, room));
    }

    @PostMapping("/{homeId}/markers")
    public ResponseEntity<HomeMarker> createMarker(@PathVariable Long homeId, @RequestBody HomeMarkerDTO markerDTO) {
        return ResponseEntity.ok(homeService.createMarker(markerDTO, homeId));
    }

    @PutMapping("/markers/{markerId}")
    public ResponseEntity<HomeMarker> updateMarker(@PathVariable Long markerId, @RequestBody HomeMarkerDTO markerDTO) {
        return ResponseEntity.ok(homeService.updateMarker(markerId, markerDTO));
    }

    @PatchMapping("/markers/{markerId}/position")
    public ResponseEntity<HomeMarker> updateMarkerPosition(
            @PathVariable Long markerId,
            @RequestBody Map<String, Integer> position) {
        Integer x = position.get("x");
        Integer y = position.get("y");
        return ResponseEntity.ok(homeService.updateMarkerPosition(markerId, x, y));
    }

    @PutMapping("/markers/positions")
    public ResponseEntity<Void> updateMarkersPositions(@RequestBody List<HomeMarker> markers) {
        homeService.updateMarkersPositions(markers);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/markers/{markerId}/status")
    public ResponseEntity<HomeMarker> toggleMarkerStatus(
            @PathVariable Long markerId,
            @RequestBody Map<String, String> request) {
        String newStatus = request.get("status");
        return ResponseEntity.ok(homeService.toggleMarkerStatus(markerId, newStatus));
    }

    @DeleteMapping("/markers/{markerId}")
    public ResponseEntity<Void> deleteMarker(@PathVariable Long markerId) {
        homeService.deleteMarker(markerId);
        return ResponseEntity.noContent().build();
    }

    // ========== STATS ENDPOINTS ==========

    @GetMapping("/{homeId}/stats")
    public ResponseEntity<HomeService.HomeStats> getStats(@PathVariable Long homeId) {
        return ResponseEntity.ok(homeService.getStats(homeId));
    }
}
