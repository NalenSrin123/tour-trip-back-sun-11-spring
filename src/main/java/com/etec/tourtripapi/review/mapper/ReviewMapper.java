package com.etec.tourtripapi.review.mapper;

import com.etec.tourtripapi.common.mapper.BaseMapper;
import com.etec.tourtripapi.review.dto.request.ReviewRequest;
import com.etec.tourtripapi.review.dto.response.ReviewResponse;
import com.etec.tourtripapi.review.entity.Review;
import com.etec.tourtripapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewMapper implements BaseMapper<Review, ReviewRequest, ReviewResponse> {

    private final UserRepository userRepository;

    @Override
    public Review toEntity(ReviewRequest request) {
        if (request == null) {
            return null;
        }

        Review review = new Review();
        updateEntity(review, request);

        if (review.getStatus() == null || review.getStatus().trim().isEmpty()) {
            review.setStatus("APPROVED");
        }

        return review;
    }

    @Override
    public ReviewResponse toResponse(Review entity) {
        if (entity == null) {
            return null;
        }

        ReviewResponse.ReviewResponseBuilder builder = ReviewResponse.builder()
                .reviewId(entity.getReviewId())
                .userId(entity.getUserId())
                .tourId(entity.getTourId())
                .rating(entity.getRating())
                .comment(entity.getComment())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt());

        if (entity.getUserId() != null) {
            userRepository.findById(entity.getUserId()).ifPresent(user -> {
                builder.userName(user.getFullName());
                builder.userEmail(user.getEmail());
                builder.userAvatar(user.getAvatarUrl());
            });
        }

        return builder.build();
    }

    @Override
    public void updateEntity(Review entity, ReviewRequest request) {
        if (entity == null || request == null) {
            return;
        }

        if (request.getUserId() != null) {
            entity.setUserId(request.getUserId());
        }
        if (request.getTourId() != null) {
            entity.setTourId(request.getTourId());
        }
        if (request.getRating() != null) {
            entity.setRating(request.getRating());
        }
        if (request.getComment() != null) {
            entity.setComment(request.getComment());
        }
    }
}
