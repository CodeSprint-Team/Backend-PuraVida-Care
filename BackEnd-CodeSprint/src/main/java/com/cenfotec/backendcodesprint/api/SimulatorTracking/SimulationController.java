package com.cenfotec.backendcodesprint.api.SimulatorTracking;

import com.cenfotec.backendcodesprint.logic.SimulatorTracking.Dto.Request.SimulatrRouterRequest;
import com.cenfotec.backendcodesprint.logic.SimulatorTracking.Dto.Response.SimulatrRouterResponse;
import com.cenfotec.backendcodesprint.logic.SimulatorTracking.Service.RouteSimulatorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simulation")
@RequiredArgsConstructor
public class SimulationController {

    private final RouteSimulatorService routeSimulatorService;

    @PostMapping("/sessions/{sessionId}/simulate-route")
    public ResponseEntity<SimulatrRouterResponse> simulateRoute(
            @PathVariable Long sessionId,
            @RequestParam Long providerProfileId,
            @Valid @RequestBody SimulatrRouterRequest requestDto) {

        SimulatrRouterResponse response = routeSimulatorService.simulateRoute(
                sessionId, providerProfileId, requestDto);

        return ResponseEntity.ok(response);
    }
}