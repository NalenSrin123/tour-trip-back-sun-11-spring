package com.etec.tourtripapi.review.service;

import com.etec.tourtripapi.review.dto.request.ReviewRequest;
import com.etec.tourtripapi.review.dto.request.ReviewStatusUpdateRequest;
import com.etec.tourtripapi.review.dto.request.UpdateReviewRequest;
import com.etec.tourtripapi.review.dto.response.ReviewResponse;
import com.etec.tourtripapi.review.dto.response.TourReviewSummaryResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse createReview(ReviewRequest request);

    ReviewResponse getReviewById(Long id);

    List<ReviewResponse> getAllReviews(Long tourId, Long userId, String status);

    List<ReviewResponse> getApprovedReviewsByTourId(Long tourId);

    List<ReviewResponse> getReviewsByUserId(Long userId);

    TourReviewSummaryResponse getTourReviewSummary(Long tourId);

    ReviewResponse updateReview(Long id, UpdateReviewRequest request);

    ReviewResponse updateReviewStatus(Long id, ReviewStatusUpdateRequest request);

    void deleteReview(Long id);
}
