package com.etec.tourtripapi.tour.service;

import com.etec.tourtripapi.tour.dto.request.TourRequest;
import com.etec.tourtripapi.tour.dto.response.TourResponse;

import java.math.BigDecimal;
import java.util.List;

public interface TourService {

    TourResponse createTour(TourRequest request);

    TourResponse getTourById(Long id);

    List<TourResponse> searchTours(
            String keyword,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer durationDays
    );

    TourResponse updateTour(Long id, TourRequest request);

    void deleteTour(Long id);

    TourResponse addImageToTour(Long tourId, String imageUrl, boolean isPrimary);

    void removeImageFromTour(Long tourId, Long imageId);

    TourResponse setPrimaryImage(Long tourId, Long imageId);
}
