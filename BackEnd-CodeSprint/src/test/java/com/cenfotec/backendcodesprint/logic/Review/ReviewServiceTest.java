package com.cenfotec.backendcodesprint.logic.Review;

import com.cenfotec.backendcodesprint.logic.Model.ProviderProfile;
import com.cenfotec.backendcodesprint.logic.Model.Review;
import com.cenfotec.backendcodesprint.logic.Review.Dto.Request.CreateReview;
import com.cenfotec.backendcodesprint.logic.Review.Dto.Request.UpdateReview;
import com.cenfotec.backendcodesprint.logic.Review.Dto.Response.ReviewResponse;
import com.cenfotec.backendcodesprint.logic.Review.Mapper.ReviewMapper;
import com.cenfotec.backendcodesprint.logic.Review.Repository.ReviewRepository;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ProviderProfileRepository;
import com.cenfotec.backendcodesprint.logic.Review.Service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepo;

    @Mock
    private ProviderProfileRepository providerRepo;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService reviewService;

    private ProviderProfile provider;
    private Review review;
    private ReviewResponse response;

    @BeforeEach
    void setUp() {
        provider = new ProviderProfile();
        provider.setId(1L);
        provider.setAverageRating(BigDecimal.ZERO);

        review = new Review();
        review.setId(10L);
        review.setProviderProfile(provider);
        review.setRanking(BigDecimal.valueOf(5));

        response = new ReviewResponse();
    }

    @Test
    void createReview_ShouldCreateReviewSuccessfully() {

        CreateReview dto = new CreateReview();
        dto.setServiceBookingId(100L);
        dto.setClientProfileId(20L);

        when(providerRepo.findById(1L)).thenReturn(Optional.of(provider));
        when(reviewRepo.existsByServiceBooking_IdAndClientProfile_Id(100L, 20L)).thenReturn(false);
        when(reviewMapper.toEntity(dto)).thenReturn(review);
        when(reviewRepo.save(review)).thenReturn(review);
        when(reviewRepo.findByProviderProfile_Id(1L)).thenReturn(List.of(review));
        when(reviewMapper.toResponse(review)).thenReturn(response);

        ReviewResponse result = reviewService.createReview(1L, dto);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(5.0), provider.getAverageRating());

        verify(reviewRepo, times(1)).save(review);
        verify(providerRepo, times(1)).save(provider);
    }

    @Test
    void createReview_WhenProviderNotFound_ShouldThrowException() {

        CreateReview dto = new CreateReview();

        when(providerRepo.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reviewService.createReview(1L, dto);
        });

        assertTrue(exception.getMessage().contains("Provider not found"));
    }

    @Test
    void createReview_WhenClientAlreadyReviewed_ShouldThrowException() {

        CreateReview dto = new CreateReview();
        dto.setServiceBookingId(100L);
        dto.setClientProfileId(20L);

        when(providerRepo.findById(1L)).thenReturn(Optional.of(provider));
        when(reviewRepo.existsByServiceBooking_IdAndClientProfile_Id(100L, 20L)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reviewService.createReview(1L, dto);
        });

        assertTrue(exception.getMessage().contains("Ya existe una reseña para esta reserva"));
    }

    @Test
    void updateReview_ShouldUpdateReviewSuccessfully() {

        UpdateReview dto = new UpdateReview();

        review.setRanking(BigDecimal.valueOf(4));

        when(reviewRepo.findById(10L)).thenReturn(Optional.of(review));
        when(reviewRepo.save(review)).thenReturn(review);
        when(reviewRepo.findByProviderProfile_Id(1L)).thenReturn(List.of(review));
        when(reviewMapper.toResponse(review)).thenReturn(response);

        ReviewResponse result = reviewService.updateReview(10L, dto);

        assertNotNull(result);

        verify(reviewMapper, times(1)).updateEntity(review, dto);
        verify(reviewRepo, times(1)).save(review);
        verify(providerRepo, times(1)).save(provider);
    }

    @Test
    void updateReview_WhenReviewNotFound_ShouldThrowException() {

        UpdateReview dto = new UpdateReview();

        when(reviewRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reviewService.updateReview(99L, dto);
        });

        assertTrue(exception.getMessage().contains("Review not found"));
    }

    @Test
    void deleteReview_ShouldDeleteReviewSuccessfully() {

        when(reviewRepo.findById(10L)).thenReturn(Optional.of(review));
        when(reviewRepo.findByProviderProfile_Id(1L)).thenReturn(List.of());

        reviewService.deleteReview(10L);

        verify(reviewRepo, times(1)).delete(review);
        verify(providerRepo, times(1)).save(provider);

        assertEquals(BigDecimal.ZERO, provider.getAverageRating());
    }

    @Test
    void deleteReview_WhenReviewNotFound_ShouldThrowException() {

        when(reviewRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reviewService.deleteReview(99L);
        });

        assertTrue(exception.getMessage().contains("Review not found"));
    }

    @Test
    void getReviewsByProvider_ShouldReturnReviewList() {

        when(reviewRepo.findByProviderProfile_Id(1L)).thenReturn(List.of(review));
        when(reviewMapper.toResponse(review)).thenReturn(response);

        List<ReviewResponse> result = reviewService.getReviewsByProvider(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getReview_ShouldReturnReviewSuccessfully() {

        when(reviewRepo.findById(10L)).thenReturn(Optional.of(review));
        when(reviewMapper.toResponse(review)).thenReturn(response);

        ReviewResponse result = reviewService.getReview(10L);

        assertNotNull(result);
    }

    @Test
    void getReview_WhenReviewNotFound_ShouldThrowException() {

        when(reviewRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reviewService.getReview(99L);
        });

        assertTrue(exception.getMessage().contains("Review not found"));
    }
}
