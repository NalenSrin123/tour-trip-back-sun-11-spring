package com.etec.tourtripapi.review.controller;

import com.etec.tourtripapi.common.response.ApiResponse;
import com.etec.tourtripapi.review.dto.request.ReviewRequest;
import com.etec.tourtripapi.review.dto.request.ReviewStatusUpdateRequest;
import com.etec.tourtripapi.review.dto.request.UpdateReviewRequest;
import com.etec.tourtripapi.review.dto.response.ReviewResponse;
import com.etec.tourtripapi.review.dto.response.TourReviewSummaryResponse;
import com.etec.tourtripapi.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * GET /api/v1/reviews
     * Retrieve all reviews with optional filters by tourId, userId, or status.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getAllReviews(
            @RequestParam(required = false) Long tourId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String status) {

        List<ReviewResponse> reviews = reviewService.getAllReviews(tourId, userId, status);
        return ResponseEntity.ok(ApiResponse.success("Reviews retrieved successfully", reviews));
    }

    /**
     * GET /api/v1/reviews/{id}
     * Retrieve a single review by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReviewById(@PathVariable Long id) {
        ReviewResponse review = reviewService.getReviewById(id);
        return ResponseEntity.ok(ApiResponse.success("Review retrieved successfully", review));
    }

    /**
     * GET /api/v1/reviews/tour/{tourId}
     * Retrieve all approved reviews for a specific tour.
     */
    @GetMapping("/tour/{tourId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getApprovedReviewsByTourId(@PathVariable Long tourId) {
        List<ReviewResponse> reviews = reviewService.getApprovedReviewsByTourId(tourId);
        return ResponseEntity.ok(ApiResponse.success("Tour reviews retrieved successfully", reviews));
    }

    /**
     * GET /api/v1/reviews/tour/{tourId}/summary
     * Retrieve aggregate rating and review count summary for a tour.
     */
    @GetMapping("/tour/{tourId}/summary")
    public ResponseEntity<ApiResponse<TourReviewSummaryResponse>> getTourReviewSummary(@PathVariable Long tourId) {
        TourReviewSummaryResponse summary = reviewService.getTourReviewSummary(tourId);
        return ResponseEntity.ok(ApiResponse.success("Tour review summary retrieved successfully", summary));
    }

    /**
     * GET /api/v1/reviews/user/{userId}
     * Retrieve all reviews submitted by a specific user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("User reviews retrieved successfully", reviews));
    }

    /**
     * POST /api/v1/reviews
     * Submit a new feedback and rating review for a tour.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(@Valid @RequestBody ReviewRequest request) {
        ReviewResponse created = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Review submitted successfully", created));
    }

    /**
     * PUT /api/v1/reviews/{id}
     * Update rating and comment of an existing review.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReviewRequest request) {

        ReviewResponse updated = reviewService.updateReview(id, request);
        return ResponseEntity.ok(ApiResponse.success("Review updated successfully", updated));
    }

    /**
     * PATCH /api/v1/reviews/{id}/status
     * Admin endpoint: Moderate review status (APPROVED, REJECTED, PENDING).
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReviewStatus(
            @PathVariable Long id,
            @Valid @RequestBody ReviewStatusUpdateRequest request) {

        ReviewResponse updated = reviewService.updateReviewStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Review status updated successfully", updated));
    }

    /**
     * DELETE /api/v1/reviews/{id}
     * Delete a review by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.success("Review deleted successfully"));
    }
}
