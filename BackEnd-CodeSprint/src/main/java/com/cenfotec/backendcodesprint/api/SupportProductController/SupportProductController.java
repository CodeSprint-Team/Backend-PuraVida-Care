package com.cenfotec.backendcodesprint.api.SupportProductController;

import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Request.CreateSupportProductPostRequestDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Request.UpdateSupportProductPostRequestDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Response.SupportProductPostResponseDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.Service.SupportProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/support-products")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SupportProductController {

    private final SupportProductService supportProductService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> createPost(
            @ModelAttribute CreateSupportProductPostRequestDTO data,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        supportProductService.createPost(data, image);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Publicación creada correctamente"));
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