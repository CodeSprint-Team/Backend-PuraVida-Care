package com.cenfotec.backendcodesprint.logic.Review.Service;

import com.cenfotec.backendcodesprint.logic.Model.*;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ProviderProfileRepository;
import com.cenfotec.backendcodesprint.logic.Review.Dto.Request.CreateReview;
import com.cenfotec.backendcodesprint.logic.Review.Dto.Request.UpdateReview;
import com.cenfotec.backendcodesprint.logic.Review.Dto.Response.ReviewResponse;
import com.cenfotec.backendcodesprint.logic.Review.Mapper.ReviewMapper;
import com.cenfotec.backendcodesprint.logic.Review.Repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository          reviewRepo;
    private final ProviderProfileRepository providerRepo;
    private final ReviewMapper              reviewMapper;

    @Transactional
    public ReviewResponse createReview(Long providerProfileId, CreateReview dto) {
        ProviderProfile provider = providerRepo.findById(providerProfileId)
                .orElseThrow(() -> new RuntimeException("Provider not found: " + providerProfileId));

        // Validar que no exista ya una reseña para este booking
        if (dto.getClientProfileId() != null
                && reviewRepo.existsByServiceBooking_IdAndClientProfile_Id(dto.getServiceBookingId(), dto.getClientProfileId())) {
            throw new RuntimeException("Ya existe una reseña para esta reserva");
        }
        if (dto.getSeniorProfileId() != null
                && reviewRepo.existsByServiceBooking_IdAndSeniorProfile_Id(dto.getServiceBookingId(), dto.getSeniorProfileId())) {
            throw new RuntimeException("Ya existe una reseña para esta reserva");
        }

        Review review = reviewMapper.toEntity(dto);
        review.setProviderProfile(provider);
        Review saved = reviewRepo.save(review);

        recalculateAverageRating(providerProfileId, provider);

        return reviewMapper.toResponse(saved);
    }

    @Transactional
    public ReviewResponse updateReview(Long reviewId, UpdateReview dto) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found: " + reviewId));

        reviewMapper.updateEntity(review, dto);
        Review saved = reviewRepo.save(review);

        recalculateAverageRating(review.getProviderProfile().getId(), review.getProviderProfile());

        return reviewMapper.toResponse(saved);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found: " + reviewId));

        Long providerProfileId = review.getProviderProfile().getId();
        ProviderProfile provider = review.getProviderProfile();

        reviewRepo.delete(review);

        recalculateAverageRating(providerProfileId, provider);
    }

    public List<ReviewResponse> getReviewsByProvider(Long providerProfileId) {
        return reviewRepo.findByProviderProfile_Id(providerProfileId).stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ReviewResponse getReview(Long reviewId) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found: " + reviewId));
        return reviewMapper.toResponse(review);
    }

    private void recalculateAverageRating(Long providerProfileId, ProviderProfile provider) {
        List<Review> allReviews = reviewRepo.findByProviderProfile_Id(providerProfileId);
        if (!allReviews.isEmpty()) {
            double avg = allReviews.stream()
                    .mapToDouble(r -> r.getRanking().doubleValue())
                    .average()
                    .orElse(0.0);
            provider.setAverageRating(BigDecimal.valueOf(Math.round(avg * 10.0) / 10.0));
        } else {
            provider.setAverageRating(BigDecimal.ZERO);
        }
        providerRepo.save(provider);
    }
}