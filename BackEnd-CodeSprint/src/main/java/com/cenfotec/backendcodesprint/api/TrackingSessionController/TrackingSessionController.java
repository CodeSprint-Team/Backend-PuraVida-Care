package com.cenfotec.backendcodesprint.api.TrackingSessionController;

import com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Request.TrackingPointRequestDto;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Request.TrackingSessionRequestDto;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Response.TrackingPointResponseDto;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Dto.Response.TrackingSessionResponseDto;
import com.cenfotec.backendcodesprint.logic.TrakingSesion.Service.TrackingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tracking")
@RequiredArgsConstructor
public class TrackingSessionController {

    private final TrackingService trackingService;

    @PostMapping("/sessions")
    public ResponseEntity<TrackingSessionResponseDto> startTraking(
            @RequestParam Long providerProfileId,
            @Valid @RequestBody TrackingSessionRequestDto trackingSessionRequestDto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(trackingService.startTrackingSession(trackingSessionRequestDto, providerProfileId));
    }

    @PatchMapping("/sessions/{sessionId}/end")
    public ResponseEntity<TrackingSessionResponseDto> endTraking(
            @PathVariable Long sessionId,
            @RequestParam Long providerProfileId){
        return ResponseEntity.ok(trackingService.stopTrackingSession(sessionId, providerProfileId));
    }

    @PatchMapping("/sessions/{sessionId}/points")
    public ResponseEntity<TrackingPointResponseDto> addPoints(
            @PathVariable Long sessionId,
            @RequestParam Long providerProfileId,
            @Valid @RequestBody TrackingPointRequestDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(trackingService.addPoint(sessionId, dto, providerProfileId));
    }


}
