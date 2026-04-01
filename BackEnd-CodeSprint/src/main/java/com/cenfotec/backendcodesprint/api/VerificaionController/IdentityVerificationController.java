package com.cenfotec.backendcodesprint.api.VerificaionController;

import com.cenfotec.backendcodesprint.logic.BiometricVerification.Dto.Request.UpdateRequestVerificationDto;
import com.cenfotec.backendcodesprint.logic.BiometricVerification.Dto.Response.IdentityVerificationResponseDto;
import com.cenfotec.backendcodesprint.logic.BiometricVerification.Service.IdentityVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/verifications")
@RequiredArgsConstructor
public class IdentityVerificationController {

    private final IdentityVerificationService service;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<IdentityVerificationResponseDto> create(@RequestParam Long userId, @RequestPart MultipartFile idFront,
                                                                  @RequestPart MultipartFile idBack,
                                                                  @RequestPart MultipartFile selfie) {

        return ResponseEntity.ok(service.create(userId, idFront, idBack, selfie));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<IdentityVerificationResponseDto> updateStatus(@PathVariable Long id, @RequestBody UpdateRequestVerificationDto dto) {
        return ResponseEntity.ok(service.updateStatus(id, dto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<IdentityVerificationResponseDto>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findByUser(userId));
    }

    @GetMapping
    public ResponseEntity<List<IdentityVerificationResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
}
