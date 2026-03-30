package com.cenfotec.backendcodesprint.api.ReviewController;


import com.cenfotec.backendcodesprint.logic.Review.Dto.Request.CreateReview;
import com.cenfotec.backendcodesprint.logic.Review.Dto.Request.UpdateReview;
import com.cenfotec.backendcodesprint.logic.Review.Dto.Response.ReviewResponse;
import com.cenfotec.backendcodesprint.logic.Review.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews/providers/{providerId}")
@RequiredArgsConstructor
public class ReviewsController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> create(
            @PathVariable Long providerId,
            @Valid @RequestBody CreateReview dto) {
        return ResponseEntity.ok(reviewService.createReview(providerId, dto));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> update(
            @PathVariable Long providerId,
            @PathVariable Long reviewId,
            @Valid @RequestBody UpdateReview dto) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, dto));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long providerId,
            @PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getByProvider(
            @PathVariable Long providerId) {
        return ResponseEntity.ok(reviewService.getReviewsByProvider(providerId));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getOne(
            @PathVariable Long providerId,
            @PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReview(reviewId));
    }
}