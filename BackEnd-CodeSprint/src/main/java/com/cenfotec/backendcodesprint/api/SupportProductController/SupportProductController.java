package com.cenfotec.backendcodesprint.api.SupportProductController;

import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Request.CreateSupportProductPostRequestDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Request.UpdateSupportProductPostRequestDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Response.SupportProductPostResponseDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.Service.SupportProductService;
import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Request.OfferActionRequestDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Request.CreateArticleOfferRequestDTO;
import com.cenfotec.backendcodesprint.logic.SupportProduct.DTO.Response.ArticleOfferResponseDTO;
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

    @PatchMapping("/offers/{offerId}/accept")
    public ResponseEntity<ArticleOfferResponseDTO> acceptOffer(
            @PathVariable Long offerId,
            @RequestBody OfferActionRequestDTO dto
    ) {
        System.out.println("ENTRO AL CONTROLLER ACCEPT");
        System.out.println("offerId = " + offerId);
        System.out.println("ownerUserId = " + dto.getOwnerUserId());

        return ResponseEntity.ok(supportProductService.acceptOffer(offerId, dto.getOwnerUserId()));
    }

    @PatchMapping("/offers/{offerId}/reject")
    public ResponseEntity<ArticleOfferResponseDTO> rejectOffer(
            @PathVariable Long offerId,
            @RequestBody OfferActionRequestDTO dto
    ) {
        return ResponseEntity.ok(supportProductService.rejectOffer(offerId, dto.getOwnerUserId()));
    }
    @PostMapping("/offers")
    public ResponseEntity<ArticleOfferResponseDTO> createOffer(
            @RequestBody CreateArticleOfferRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(supportProductService.createOffer(dto));
    }

    @GetMapping("/offers/post/{postId}")
    public ResponseEntity<List<ArticleOfferResponseDTO>> getOffersByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(supportProductService.getOffersByPostId(postId));
    }

    @GetMapping("/offers/received/{ownerUserId}")
    public ResponseEntity<List<ArticleOfferResponseDTO>> getOffersReceived(@PathVariable Long ownerUserId) {
        return ResponseEntity.ok(supportProductService.getOffersReceivedByOwner(ownerUserId));
    }

    @GetMapping("/offers/made/{buyerUserId}")
    public ResponseEntity<List<ArticleOfferResponseDTO>> getOffersMade(@PathVariable Long buyerUserId) {
        return ResponseEntity.ok(supportProductService.getOffersMadeByBuyer(buyerUserId));
    }

}