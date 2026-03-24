package com.cenfotec.backendcodesprint.api.AdminController;

import com.cenfotec.backendcodesprint.logic.Admin.DTO.ProviderPendingDTO;
import com.cenfotec.backendcodesprint.logic.Admin.DTO.ReviewProviderDTO;
import com.cenfotec.backendcodesprint.logic.Admin.DTO.ReviewUserDTO;
import com.cenfotec.backendcodesprint.logic.Admin.DTO.UserStatusDTO;
import com.cenfotec.backendcodesprint.logic.Admin.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/providers/pending")
    public ResponseEntity<List<ProviderPendingDTO>> getPendingProviders() {
        return ResponseEntity.ok(adminService.getPendingProviders());
    }

    @PutMapping("/providers/{id}/review")
    public ResponseEntity<ProviderPendingDTO> reviewProvider(
            @PathVariable Long id,
            @RequestBody ReviewProviderDTO dto) {
        return ResponseEntity.ok(adminService.reviewProvider(id, dto));
    }
    @GetMapping("/users")
    public ResponseEntity<List<UserStatusDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/{id}/review")
    public ResponseEntity<UserStatusDTO> reviewUser(
            @PathVariable Long id,
            @RequestBody ReviewUserDTO dto) {
        return ResponseEntity.ok(adminService.reviewUser(id, dto));
    }

}