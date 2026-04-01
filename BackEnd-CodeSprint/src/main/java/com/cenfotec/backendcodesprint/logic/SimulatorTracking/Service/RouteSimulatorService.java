package com.cenfotec.backendcodesprint.logic.SimulatorTracking.Service;

import com.cenfotec.backendcodesprint.api.TrackingSessionController.TrackingWebSocketController;
import com.cenfotec.backendcodesprint.logic.SimulatorTracking.Dto.Request.SimulatrRouterRequest;
import com.cenfotec.backendcodesprint.logic.SimulatorTracking.Dto.Response.SimulatrRouterResponse;
import com.cenfotec.backendcodesprint.logic.SimulatorTracking.Mapper.SimulationMapper;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Request.TrackingPointRequestDto;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Response.TrackingPointResponseDto;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Service.TrackingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteSimulatorService {

    private final TrackingService trackingService;
    private final TrackingWebSocketController webSocketController;
    private final SimulationMapper simulationMapper;
    private final ObjectMapper objectMapper;

    /**
     * Simula una ruta REAL siguiendo calles usando OSRM.
     *
     * 1. Consulta OSRM para la ruta real
     * 2. Guarda en BD SOLO origen y destino
     * 3. Devuelve todos los puntos al frontend del proveedor
     * 4. Emite los puntos por WebSocket de forma async (para el familiar)
     */
    public SimulatrRouterResponse simulateRoute(
            Long sessionId,
            Long providerProfileId,
            SimulatrRouterRequest requestDto) {

        List<double[]> waypoints = requestDto.getWaypoints();
        long intervalMs = requestDto.getIntervalMs();
        int totalSegments = waypoints.size() - 1;

        // 1. Consultar OSRM
        List<double[]> routePoints = getRouteFromOSRM(waypoints);

        if (routePoints.isEmpty()) {
            log.warn("OSRM no devolvio ruta, usando interpolacion lineal");
            routePoints = linearInterpolation(waypoints, requestDto.getPointsPerSegment());
        }

        log.info("Ruta obtenida: {} puntos por calles reales", routePoints.size());

        // 2. Guardar en BD SOLO origen y destino
        double[] origen = waypoints.get(0);
        double[] destino = waypoints.get(waypoints.size() - 1);

        TrackingPointRequestDto origenDto = new TrackingPointRequestDto();
        origenDto.setLatitude(origen[0]);
        origenDto.setLongitude(origen[1]);
        trackingService.addPoint(sessionId, origenDto, providerProfileId);

        TrackingPointRequestDto destinoDto = new TrackingPointRequestDto();
        destinoDto.setLatitude(destino[0]);
        destinoDto.setLongitude(destino[1]);
        trackingService.addPoint(sessionId, destinoDto, providerProfileId);

        // 3. Convertir a DTOs para devolver al proveedor
        List<TrackingPointResponseDto> allPoints = new ArrayList<>();
        long pointId = 1;

        for (double[] coord : routePoints) {
            TrackingPointResponseDto point = new TrackingPointResponseDto();
            point.setTrackingPointId(pointId++);
            point.setTrackingSessionId(sessionId);
            point.setLatitude(BigDecimal.valueOf(coord[0]).setScale(7, RoundingMode.HALF_UP));
            point.setLongitude(BigDecimal.valueOf(coord[1]).setScale(7, RoundingMode.HALF_UP));
            point.setRecordedAt(System.currentTimeMillis());
            allPoints.add(point);
        }

        // 4. Emitir puntos por WebSocket de forma async
        //    El familiar los recibe en tiempo real sin tocar la BD
        broadcastPointsAsync(sessionId, routePoints, intervalMs);

        log.info("Simulacion: {} puntos al proveedor, emitiendo por WebSocket", allPoints.size());
        return simulationMapper.toResponse(sessionId, allPoints, totalSegments);
    }

    /**
     * Emite los puntos por WebSocket uno por uno con delay.
     * Se ejecuta en un hilo separado para no bloquear la respuesta.
     */
    @Async
    public void broadcastPointsAsync(Long sessionId, List<double[]> routePoints, long intervalMs) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        log.info("Iniciando broadcast WebSocket: {} puntos, {}ms intervalo", routePoints.size(), intervalMs);

        for (int i = 0; i < routePoints.size(); i++) {
            double[] coord = routePoints.get(i);

            try {
                webSocketController.broadcastPoint(sessionId, coord[0], coord[1]);

                if (i < routePoints.size() - 1 && intervalMs > 0) {
                    Thread.sleep(intervalMs);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Broadcast interrumpido en punto {}", i);
                return;
            } catch (Exception e) {
                log.error("Error broadcast punto {}: {}", i, e.getMessage());
            }
        }

        log.info("Broadcast WebSocket completado: {} puntos emitidos", routePoints.size());
    }

    public List<double[]> getRouteFromOSRM(List<double[]> waypoints) {
        List<double[]> routePoints = new ArrayList<>();

        try {
            StringBuilder coords = new StringBuilder();
            for (int i = 0; i < waypoints.size(); i++) {
                if (i > 0) coords.append(";");
                coords.append(waypoints.get(i)[1]);
                coords.append(",");
                coords.append(waypoints.get(i)[0]);
            }

            String url = String.format(
                    "https://router.project-osrm.org/route/v1/driving/%s?overview=full&geometries=geojson",
                    coords
            );

            log.info("Consultando OSRM: {}", url);

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            JsonNode root = objectMapper.readTree(response);
            String status = root.path("code").asText();

            if (!"Ok".equals(status)) {
                log.error("OSRM error: {}", status);
                return routePoints;
            }

            JsonNode coordinates = root
                    .path("routes").get(0)
                    .path("geometry")
                    .path("coordinates");

            for (JsonNode coord : coordinates) {
                double lon = coord.get(0).asDouble();
                double lat = coord.get(1).asDouble();
                routePoints.add(new double[]{lat, lon});
            }

            double distanceKm = root.path("routes").get(0)
                    .path("distance").asDouble() / 1000;
            double durationMin = root.path("routes").get(0)
                    .path("duration").asDouble() / 60;

            log.info("Ruta OSRM: {} km, {} min, {} puntos",
                    String.format("%.1f", distanceKm),
                    String.format("%.0f", durationMin),
                    routePoints.size());

        } catch (Exception e) {
            log.error("Error consultando OSRM: {}", e.getMessage());
        }

        return routePoints;
    }

    private List<double[]> linearInterpolation(List<double[]> waypoints, int pointsPerSegment) {
        List<double[]> points = new ArrayList<>();

        for (int seg = 0; seg < waypoints.size() - 1; seg++) {
            double[] from = waypoints.get(seg);
            double[] to = waypoints.get(seg + 1);
            int startI = (seg == 0) ? 0 : 1;

            for (int i = startI; i <= pointsPerSegment; i++) {
                double t = (double) i / pointsPerSegment;
                double lat = from[0] + (to[0] - from[0]) * t;
                double lon = from[1] + (to[1] - from[1]) * t;
                points.add(new double[]{lat, lon});
            }
        }

        return points;
    }
}