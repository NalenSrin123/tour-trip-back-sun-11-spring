package com.etec.tourtripapi.review.service.impl;

import com.etec.tourtripapi.common.exception.BadRequestException;
import com.etec.tourtripapi.common.exception.ResourceNotFoundException;
import com.etec.tourtripapi.review.dto.request.ReviewRequest;
import com.etec.tourtripapi.review.dto.request.ReviewStatusUpdateRequest;
import com.etec.tourtripapi.review.dto.request.UpdateReviewRequest;
import com.etec.tourtripapi.review.dto.response.ReviewResponse;
import com.etec.tourtripapi.review.dto.response.TourReviewSummaryResponse;
import com.etec.tourtripapi.review.entity.Review;
import com.etec.tourtripapi.review.mapper.ReviewMapper;
import com.etec.tourtripapi.review.repository.ReviewRepository;
import com.etec.tourtripapi.review.service.ReviewService;
import com.etec.tourtripapi.security.userdetails.CustomUserDetails;
import com.etec.tourtripapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewResponse createReview(ReviewRequest request) {
        Long resolvedUserId = request.getUserId();

        // If userId not specified in request, attempt to extract from authenticated principal
        if (resolvedUserId == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
                resolvedUserId = userDetails.getUser().getUserId();
            }
        }

        if (resolvedUserId == null) {
            throw new BadRequestException("User ID is required to submit a review");
        }

        if (!userRepository.existsById(resolvedUserId)) {
            throw new ResourceNotFoundException("User", "userId", resolvedUserId);
        }

        Review review = reviewMapper.toEntity(request);
        review.setUserId(resolvedUserId);

        Review savedReview = reviewRepository.save(review);
        log.info("Review created with ID: {} for tour ID: {} by user ID: {}",
                savedReview.getReviewId(), savedReview.getTourId(), savedReview.getUserId());

        return reviewMapper.toResponse(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "reviewId", id));
        return reviewMapper.toResponse(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getAllReviews(Long tourId, Long userId, String status) {
        List<Review> reviews;

        if (tourId != null && status != null && !status.trim().isEmpty()) {
            reviews = reviewRepository.findByTourIdAndStatusIgnoreCase(tourId, status.trim());
        } else if (tourId != null) {
            reviews = reviewRepository.findByTourId(tourId);
        } else if (userId != null) {
            reviews = reviewRepository.findByUserId(userId);
        } else if (status != null && !status.trim().isEmpty()) {
            reviews = reviewRepository.findByStatusIgnoreCase(status.trim());
        } else {
            reviews = reviewRepository.findAll();
        }

        return reviewMapper.toResponseList(reviews);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getApprovedReviewsByTourId(Long tourId) {
        List<Review> reviews = reviewRepository.findByTourIdAndStatusIgnoreCase(tourId, "APPROVED");
        return reviewMapper.toResponseList(reviews);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return reviewMapper.toResponseList(reviews);
    }

    @Override
    @Transactional(readOnly = true)
    public TourReviewSummaryResponse getTourReviewSummary(Long tourId) {
        List<Review> approvedReviews = reviewRepository.findByTourIdAndStatusIgnoreCase(tourId, "APPROVED");

        long totalReviews = approvedReviews.size();
        Double avgRating = reviewRepository.calculateAverageRatingForApproved(tourId);
        if (avgRating == null) {
            avgRating = 0.0;
        } else {
            // Round to 1 decimal place
            avgRating = Math.round(avgRating * 10.0) / 10.0;
        }

        Map<Integer, Long> distribution = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            distribution.put(i, 0L);
        }
        for (Review r : approvedReviews) {
            if (r.getRating() != null && r.getRating() >= 1 && r.getRating() <= 5) {
                distribution.put(r.getRating(), distribution.get(r.getRating()) + 1L);
            }
        }

        return TourReviewSummaryResponse.builder()
                .tourId(tourId)
                .averageRating(avgRating)
                .totalReviews(totalReviews)
                .ratingCounts(distribution)
                .build();
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(Long id, UpdateReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "reviewId", id));

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review updatedReview = reviewRepository.save(review);
        log.info("Review updated with ID: {}", id);
        return reviewMapper.toResponse(updatedReview);
    }

    @Override
    @Transactional
    public ReviewResponse updateReviewStatus(Long id, ReviewStatusUpdateRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "reviewId", id));

        review.setStatus(request.getStatus().trim().toUpperCase());
        Review updatedReview = reviewRepository.save(review);
        log.info("Review status updated for ID: {} to {}", id, review.getStatus());
        return reviewMapper.toResponse(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "reviewId", id));
        reviewRepository.delete(review);
        log.info("Review deleted with ID: {}", id);
    }
}
