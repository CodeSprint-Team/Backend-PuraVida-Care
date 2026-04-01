package com.cenfotec.backendcodesprint.logic.TrackingService;

import com.cenfotec.backendcodesprint.api.TrackingSessionController.TrackingWebSocketController;
import com.cenfotec.backendcodesprint.logic.SimulatorTracking.Dto.Request.SimulatrRouterRequest;
import com.cenfotec.backendcodesprint.logic.SimulatorTracking.Dto.Response.SimulatrRouterResponse;
import com.cenfotec.backendcodesprint.logic.SimulatorTracking.Mapper.SimulationMapper;
import com.cenfotec.backendcodesprint.logic.SimulatorTracking.Service.RouteSimulatorService;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Request.TrackingPointRequestDto;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Response.TrackingPointResponseDto;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Service.TrackingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteSimulatorServiceTest {

    @Mock
    private TrackingService trackingService;

    @Mock
    private TrackingWebSocketController webSocketController;

    @Mock
    private SimulationMapper simulationMapper;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private RouteSimulatorService routeSimulatorService;

    private SimulatrRouterRequest request;

    @BeforeEach
    void setUp() {
        request = new SimulatrRouterRequest();

        List<double[]> waypoints = new ArrayList<>();
        waypoints.add(new double[]{9.934, -84.087});
        waypoints.add(new double[]{9.935, -84.088});

        request.setWaypoints(waypoints);
        request.setIntervalMs(0);
        request.setPointsPerSegment(5);
    }

    @Test
    void simulateRoute_ShouldSaveOriginDestinationAndReturnResponse() throws Exception {

        RouteSimulatorService spyService = spy(routeSimulatorService);

        List<double[]> routePoints = List.of(
                new double[]{9.934, -84.087},
                new double[]{9.9345, -84.0875},
                new double[]{9.935, -84.088}
        );

        doReturn(routePoints).when(spyService).getRouteFromOSRM(any());
        doNothing().when(spyService).broadcastPointsAsync(anyLong(), anyList(), anyLong());

        when(trackingService.addPoint(eq(1L), any(TrackingPointRequestDto.class), eq(10L)))
                .thenReturn(new TrackingPointResponseDto());

        when(simulationMapper.toResponse(eq(1L), anyList(), eq(1)))
                .thenReturn(new SimulatrRouterResponse());

        SimulatrRouterResponse result = spyService.simulateRoute(1L, 10L, request);

        assertNotNull(result);

        verify(trackingService, times(2))
                .addPoint(eq(1L), any(TrackingPointRequestDto.class), eq(10L));

        verify(spyService, times(1))
                .broadcastPointsAsync(eq(1L), eq(routePoints), eq(0L));
    }

    @Test
    void simulateRoute_WhenOSRMReturnsEmpty_ShouldUseLinearInterpolation() throws Exception {

        RouteSimulatorService spyService = spy(routeSimulatorService);

        doReturn(new ArrayList<double[]>()).when(spyService).getRouteFromOSRM(any());
        doNothing().when(spyService).broadcastPointsAsync(anyLong(), anyList(), anyLong());

        when(trackingService.addPoint(eq(1L), any(TrackingPointRequestDto.class), eq(10L)))
                .thenReturn(new TrackingPointResponseDto());

        when(simulationMapper.toResponse(eq(1L), anyList(), eq(1)))
                .thenReturn(new SimulatrRouterResponse());

        SimulatrRouterResponse result = spyService.simulateRoute(1L, 10L, request);

        assertNotNull(result);

        verify(trackingService, times(2))
                .addPoint(eq(1L), any(TrackingPointRequestDto.class), eq(10L));
    }

    @Test
    void broadcastPointsAsync_ShouldBroadcastAllPoints() {

        List<double[]> routePoints = List.of(
                new double[]{9.934, -84.087},
                new double[]{9.935, -84.088}
        );

        routeSimulatorService.broadcastPointsAsync(1L, routePoints, 0);

        verify(webSocketController, times(2))
                .broadcastPoint(eq(1L), anyDouble(), anyDouble());
    }

    @Test
    void broadcastPointsAsync_WhenBroadcastFails_ShouldContinueWithNextPoint() {

        List<double[]> routePoints = List.of(
                new double[]{9.934, -84.087},
                new double[]{9.935, -84.088}
        );

        doThrow(new RuntimeException("Error WebSocket"))
                .when(webSocketController)
                .broadcastPoint(eq(1L), eq(9.934), eq(-84.087));

        assertDoesNotThrow(() -> {
            routeSimulatorService.broadcastPointsAsync(1L, routePoints, 0);
        });

        verify(webSocketController, times(2))
                .broadcastPoint(eq(1L), anyDouble(), anyDouble());
    }

    @Test
    void linearInterpolation_ShouldGenerateIntermediatePoints() throws Exception {

        Method method = RouteSimulatorService.class.getDeclaredMethod(
                "linearInterpolation",
                List.class,
                int.class
        );

        method.setAccessible(true);

        List<double[]> waypoints = List.of(
                new double[]{0.0, 0.0},
                new double[]{10.0, 10.0}
        );

        List<double[]> result = (List<double[]>) method.invoke(routeSimulatorService, waypoints, 5);

        assertNotNull(result);
        assertEquals(6, result.size());

        assertEquals(0.0, result.get(0)[0]);
        assertEquals(10.0, result.get(result.size() - 1)[0]);
    }
}
