package com.cenfotec.backendcodesprint.api.SupportProductController;

import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Request.CreateSupportProductPostRequestDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Request.UpdateSupportProductPostRequestDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Response.SupportProductPostResponseDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.Service.SupportProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/support-products")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SupportProductController {

    private final SupportProductService supportProductService;

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody CreateSupportProductPostRequestDTO data) {
        supportProductService.createPost(data);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Publicación creada correctamente");
    }

    @GetMapping
    public ResponseEntity<List<SupportProductPostResponseDTO>> getAllPosts() {
        return ResponseEntity.ok(supportProductService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportProductPostResponseDTO> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(supportProductService.getPostById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupportProductPostResponseDTO> updatePost(
            @PathVariable Long id,
            @RequestBody UpdateSupportProductPostRequestDTO dto) {
        return ResponseEntity.ok(supportProductService.updatePost(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        supportProductService.deletePost(id);
        return ResponseEntity.ok("Publicación eliminada correctamente");
    }
}