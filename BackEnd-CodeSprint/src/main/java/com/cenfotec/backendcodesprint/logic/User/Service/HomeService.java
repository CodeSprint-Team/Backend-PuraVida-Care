package com.cenfotec.backendcodesprint.logic.User.Service;

import com.cenfotec.backendcodesprint.logic.FilteredHome.Repository.HomeMarkerRepository;
import com.cenfotec.backendcodesprint.logic.Model.Home;
import com.cenfotec.backendcodesprint.logic.Model.HomeMarker;
import com.cenfotec.backendcodesprint.logic.User.DTO.Home.HomeMarkerDTO;
import com.cenfotec.backendcodesprint.logic.User.Repository.HomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final HomeRepository homeRepository;
    private final HomeMarkerRepository homeMarkerRepository;

    // ========== HOME ==========

    public Home getHomeByElderlyId(Long elderlyId) {
        return homeRepository.findByElderlyIdAndStatus(elderlyId, "active")
                .orElse(null);
    }

    public Home createHome(Home home) {
        home.setStatus("active");
        return homeRepository.save(home);
    }

    public Home updateHome(Long id, Home homeDetails) {
        Home home = homeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Home not found"));
        home.setName(homeDetails.getName());
        home.setAddress(homeDetails.getAddress());
        return homeRepository.save(home);
    }

    // ========== MARKERS ==========

    public List<HomeMarker> getMarkersByHomeId(Long homeId) {
        return homeMarkerRepository.findByHomeId(homeId);
    }

    public List<HomeMarker> getMarkersByHomeIdAndType(Long homeId, String type) {
        return homeMarkerRepository.findByHomeIdAndType(homeId, type);
    }

    public List<HomeMarker> getActiveMarkersByHomeId(Long homeId) {
        return homeMarkerRepository.findByHomeIdAndStatus(homeId, "active");
    }

    public List<HomeMarker> getMarkersByHomeIdAndRoom(Long homeId, String room) {
        return homeMarkerRepository.findByHomeIdAndRoom(homeId, room);
    }

    @Transactional
    public HomeMarker createMarker(HomeMarkerDTO markerDTO, Long homeId) {
        HomeMarker marker = new HomeMarker();
        marker.setHomeId(homeId);
        marker.setRoom(markerDTO.getRoom() != null ? markerDTO.getRoom() : "main");
        marker.setType(markerDTO.getType());
        marker.setTitle(markerDTO.getTitle());
        marker.setDescription(markerDTO.getDescription());
        marker.setContact(markerDTO.getContact());
        marker.setStatus(markerDTO.getStatus() != null ? markerDTO.getStatus() : "active");
        marker.setPositionX(markerDTO.getPositionX());
        marker.setPositionY(markerDTO.getPositionY());
        marker.setIcon(markerDTO.getIcon());
        return homeMarkerRepository.save(marker);
    }

    @Transactional
    public HomeMarker updateMarker(Long markerId, HomeMarkerDTO markerDTO) {
        HomeMarker marker = homeMarkerRepository.findById(markerId)
                .orElseThrow(() -> new RuntimeException("Marker not found"));
        marker.setTitle(markerDTO.getTitle());
        marker.setDescription(markerDTO.getDescription());
        marker.setContact(markerDTO.getContact());
        marker.setPositionX(markerDTO.getPositionX());
        marker.setPositionY(markerDTO.getPositionY());
        marker.setIcon(markerDTO.getIcon());
        return homeMarkerRepository.save(marker);
    }

    @Transactional
    public HomeMarker updateMarkerPosition(Long markerId, Integer x, Integer y) {
        HomeMarker marker = homeMarkerRepository.findById(markerId)
                .orElseThrow(() -> new RuntimeException("Marker not found"));
        marker.setPositionX(x);
        marker.setPositionY(y);
        return homeMarkerRepository.save(marker);
    }

    @Transactional
    public void updateMarkersPositions(List<HomeMarker> markers) {
        for (HomeMarker marker : markers) {
            homeMarkerRepository.save(marker);
        }
    }

    @Transactional
    public HomeMarker toggleMarkerStatus(Long markerId, String newStatus) {
        HomeMarker marker = homeMarkerRepository.findById(markerId)
                .orElseThrow(() -> new RuntimeException("Marker not found"));
        marker.setStatus(newStatus);
        return homeMarkerRepository.save(marker);
    }

    @Transactional
    public void deleteMarker(Long markerId) {
        homeMarkerRepository.deleteById(markerId);
    }

    // ========== STATS ==========

    public HomeStats getStats(Long homeId) {
        List<HomeMarker> markers = homeMarkerRepository.findByHomeId(homeId);
        List<HomeMarker> activeMarkers = markers.stream()
                .filter(m -> "active".equals(m.getStatus()))
                .collect(Collectors.toList());

        HomeStats stats = new HomeStats();
        stats.setTotalMarkers(markers.size());
        stats.setActiveMarkers(activeMarkers.size());
        stats.setTypes(markers.stream()
                .collect(Collectors.groupingBy(HomeMarker::getType, Collectors.counting())));
        stats.setRooms(markers.stream()
                .collect(Collectors.groupingBy(HomeMarker::getRoom, Collectors.counting())));

        return stats;
    }

    @lombok.Data
    public static class HomeStats {
        private int totalMarkers;
        private int activeMarkers;
        private Map<String, Long> types;
        private Map<String, Long> rooms;
    }
}
