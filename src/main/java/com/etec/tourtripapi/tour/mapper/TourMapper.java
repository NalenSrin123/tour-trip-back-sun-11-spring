package com.etec.tourtripapi.tour.mapper;

import com.etec.tourtripapi.common.mapper.BaseMapper;
import com.etec.tourtripapi.tour.dto.request.TourRequest;
import com.etec.tourtripapi.tour.dto.response.TourImageResponse;
import com.etec.tourtripapi.tour.dto.response.TourResponse;
import com.etec.tourtripapi.tour.entity.Tour;
import com.etec.tourtripapi.tour.entity.TourImage;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class TourMapper implements BaseMapper<Tour, TourRequest, TourResponse> {

    @Override
    public Tour toEntity(TourRequest request) {
        if (request == null) {
            return null;
        }

        Tour tour = new Tour();
        updateEntity(tour, request);
        return tour;
    }

    @Override
    public TourResponse toResponse(Tour entity) {
        if (entity == null) {
            return null;
        }

        String primaryImageUrl = null;
        if (entity.getPrimaryImage() != null) {
            primaryImageUrl = entity.getPrimaryImage().getImageUrl();
        } else if (entity.getImages() != null && !entity.getImages().isEmpty()) {
            primaryImageUrl = entity.getImages().stream()
                    .filter(img -> Boolean.TRUE.equals(img.getIsPrimary()))
                    .findFirst()
                    .map(img -> img.getImageUrl())
                    .orElse(entity.getImages().get(0).getImageUrl());
        }

        List<TourImageResponse> imageResponses = Collections.emptyList();
        if (entity.getImages() != null) {
            imageResponses = entity.getImages().stream()
                    .map(this::toImageResponse)
                    .toList();
        }

        int scheduleCount = entity.getSchedules() != null ? entity.getSchedules().size() : 0;

        return TourResponse.builder()
                .tourId(entity.getTourId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .durationDays(entity.getDurationDays())
                .basePrice(entity.getBasePrice())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getCategoryId() : null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : null)
                .primaryImageUrl(primaryImageUrl)
                .images(imageResponses)
                .scheduleCount(scheduleCount)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Override
    public void updateEntity(Tour entity, TourRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setDurationDays(request.getDurationDays());
        entity.setBasePrice(request.getBasePrice());
    }

    public TourImageResponse toImageResponse(TourImage image) {
        if (image == null) {
            return null;
        }

        return TourImageResponse.builder()
                .tourImageId(image.getTourImageId())
                .imageUrl(image.getImageUrl())
                .isPrimary(image.getIsPrimary())
                .createdAt(image.getCreatedAt())
                .build();
    }
}
