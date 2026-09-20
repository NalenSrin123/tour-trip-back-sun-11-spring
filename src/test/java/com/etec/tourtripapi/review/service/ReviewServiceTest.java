package com.etec.tourtripapi.review.service;

import com.etec.tourtripapi.common.exception.ResourceNotFoundException;
import com.etec.tourtripapi.review.dto.request.ReviewRequest;
import com.etec.tourtripapi.review.dto.request.ReviewStatusUpdateRequest;
import com.etec.tourtripapi.review.dto.request.UpdateReviewRequest;
import com.etec.tourtripapi.review.dto.response.ReviewResponse;
import com.etec.tourtripapi.review.dto.response.TourReviewSummaryResponse;
import com.etec.tourtripapi.review.entity.Review;
import com.etec.tourtripapi.review.mapper.ReviewMapper;
import com.etec.tourtripapi.review.repository.ReviewRepository;
import com.etec.tourtripapi.review.service.impl.ReviewServiceImpl;
import com.etec.tourtripapi.user.entity.User;
import com.etec.tourtripapi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private ReviewMapper reviewMapper;
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        userRepository = mock(UserRepository.class);
        reviewMapper = new ReviewMapper(userRepository);
        reviewService = new ReviewServiceImpl(reviewRepository, userRepository, reviewMapper);
    }

    @Test
    void createReview_Success() {
        ReviewRequest request = ReviewRequest.builder()
                .userId(1L)
                .tourId(5L)
                .rating(5)
                .comment("Excellent trip!")
                .build();

        User mockUser = User.builder()
                .userId(1L)
                .fullName("John Doe")
                .email("john@example.com")
                .build();

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        Review savedReview = Review.builder()
                .reviewId(10L)
                .userId(1L)
                .tourId(5L)
                .rating(5)
                .comment("Excellent trip!")
                .status("APPROVED")
                .build();

        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);

        ReviewResponse response = reviewService.createReview(request);

        assertNotNull(response);
        assertEquals(10L, response.getReviewId());
        assertEquals(5, response.getRating());
        assertEquals("Excellent trip!", response.getComment());
        assertEquals("John Doe", response.getUserName());
        assertEquals("APPROVED", response.getStatus());
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void getReviewById_Found() {
        Review review = Review.builder()
                .reviewId(1L)
                .userId(2L)
                .tourId(3L)
                .rating(4)
                .comment("Very good")
                .status("APPROVED")
                .build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        ReviewResponse response = reviewService.getReviewById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getReviewId());
        assertEquals(4, response.getRating());
        assertEquals("APPROVED", response.getStatus());
    }

    @Test
    void getReviewById_NotFound() {
        when(reviewRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reviewService.getReviewById(999L));
    }

    @Test
    void getApprovedReviewsByTourId_ReturnsApprovedOnly() {
        Review r1 = Review.builder().reviewId(1L).tourId(5L).rating(5).status("APPROVED").build();
        Review r2 = Review.builder().reviewId(2L).tourId(5L).rating(4).status("APPROVED").build();

        when(reviewRepository.findByTourIdAndStatusIgnoreCase(5L, "APPROVED")).thenReturn(List.of(r1, r2));

        List<ReviewResponse> responses = reviewService.getApprovedReviewsByTourId(5L);

        assertEquals(2, responses.size());
    }

    @Test
    void getTourReviewSummary_CalculatesAverageAndDistribution() {
        Review r1 = Review.builder().reviewId(1L).tourId(5L).rating(5).status("APPROVED").build();
        Review r2 = Review.builder().reviewId(2L).tourId(5L).rating(4).status("APPROVED").build();

        when(reviewRepository.findByTourIdAndStatusIgnoreCase(5L, "APPROVED")).thenReturn(List.of(r1, r2));
        when(reviewRepository.calculateAverageRatingForApproved(5L)).thenReturn(4.5);

        TourReviewSummaryResponse summary = reviewService.getTourReviewSummary(5L);

        assertNotNull(summary);
        assertEquals(5L, summary.getTourId());
        assertEquals(4.5, summary.getAverageRating());
        assertEquals(2L, summary.getTotalReviews());
        assertEquals(1L, summary.getRatingCounts().get(5));
        assertEquals(1L, summary.getRatingCounts().get(4));
        assertEquals(0L, summary.getRatingCounts().get(1));
    }

    @Test
    void updateReview_UpdatesRatingAndComment() {
        Review existing = Review.builder()
                .reviewId(1L)
                .userId(2L)
                .tourId(3L)
                .rating(3)
                .comment("Old comment")
                .status("APPROVED")
                .build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateReviewRequest updateRequest = UpdateReviewRequest.builder()
                .rating(5)
                .comment("Updated comment")
                .build();

        ReviewResponse response = reviewService.updateReview(1L, updateRequest);

        assertNotNull(response);
        assertEquals(5, response.getRating());
        assertEquals("Updated comment", response.getComment());
    }

    @Test
    void updateReviewStatus_ModeratesStatus() {
        Review existing = Review.builder()
                .reviewId(1L)
                .status("PENDING")
                .build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReviewStatusUpdateRequest statusRequest = ReviewStatusUpdateRequest.builder()
                .status("REJECTED")
                .build();

        ReviewResponse response = reviewService.updateReviewStatus(1L, statusRequest);

        assertNotNull(response);
        assertEquals("REJECTED", response.getStatus());
    }

    @Test
    void deleteReview_Success() {
        Review existing = Review.builder().reviewId(1L).build();
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(existing));

        reviewService.deleteReview(1L);

        verify(reviewRepository).delete(existing);
    }
}
