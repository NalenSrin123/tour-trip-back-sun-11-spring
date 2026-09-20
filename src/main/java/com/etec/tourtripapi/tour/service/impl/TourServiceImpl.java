package com.etec.tourtripapi.tour.service.impl;

import com.etec.tourtripapi.category.entity.Category;
import com.etec.tourtripapi.category.repository.CategoryRepository;
import com.etec.tourtripapi.common.exception.BadRequestException;
import com.etec.tourtripapi.common.exception.ResourceNotFoundException;
import com.etec.tourtripapi.tour.dto.request.TourRequest;
import com.etec.tourtripapi.tour.dto.response.TourResponse;
import com.etec.tourtripapi.tour.entity.Tour;
import com.etec.tourtripapi.tour.entity.TourImage;
import com.etec.tourtripapi.tour.mapper.TourMapper;
import com.etec.tourtripapi.tour.repository.TourImageRepository;
import com.etec.tourtripapi.tour.repository.TourRepository;
import com.etec.tourtripapi.tour.service.TourService;
import com.etec.tourtripapi.tour.specification.TourSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final TourImageRepository tourImageRepository;
    private final CategoryRepository categoryRepository;
    private final TourMapper tourMapper;

    @Override
    @Transactional
    public TourResponse createTour(TourRequest request) {
        Tour tour = tourMapper.toEntity(request);

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            tour.setCategory(category);
        }

        // Handle initial images if provided
        TourImage primaryImage = null;
        if (hasText(request.getPrimaryImageUrl())) {
            primaryImage = TourImage.builder()
                    .imageUrl(request.getPrimaryImageUrl().trim())
                    .isPrimary(true)
                    .tour(tour)
                    .build();
            tour.addImage(primaryImage);
        }

        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            for (String url : request.getImageUrls()) {
                if (hasText(url) && (primaryImage == null || !url.trim().equals(primaryImage.getImageUrl()))) {
                    TourImage image = TourImage.builder()
                            .imageUrl(url.trim())
                            .isPrimary(primaryImage == null)
                            .tour(tour)
                            .build();
                    tour.addImage(image);
                    if (primaryImage == null) {
                        primaryImage = image;
                    }
                }
            }
        }

        Tour savedTour = tourRepository.save(tour);

        // Link primary image reference if created
        if (primaryImage != null) {
            savedTour.setPrimaryImage(primaryImage);
            savedTour = tourRepository.save(savedTour);
        }

        return tourMapper.toResponse(savedTour);
    }

    @Override
    @Transactional(readOnly = true)
    public TourResponse getTourById(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour", "id", id));
        return tourMapper.toResponse(tour);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourResponse> searchTours(
            String keyword,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer durationDays) {

        Specification<Tour> spec = null;

        if (hasText(keyword)) {
            spec = addSpecification(spec, TourSpecification.keywordContains(keyword.trim()));
        }

        if (categoryId != null) {
            spec = addSpecification(spec, TourSpecification.categoryEquals(categoryId));
        }

        if (minPrice != null) {
            spec = addSpecification(spec, TourSpecification.priceGreaterThanOrEqualTo(minPrice));
        }

        if (maxPrice != null) {
            spec = addSpecification(spec, TourSpecification.priceLessThanOrEqualTo(maxPrice));
        }

        if (durationDays != null) {
            spec = addSpecification(spec, TourSpecification.durationDaysEquals(durationDays));
        }

        List<Tour> tours = (spec == null) ? tourRepository.findAll() : tourRepository.findAll(spec);
        return tourMapper.toResponseList(tours);
    }

    @Override
    @Transactional
    public TourResponse updateTour(Long id, TourRequest request) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour", "id", id));

        tourMapper.updateEntity(tour, request);

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
            tour.setCategory(category);
        } else {
            tour.setCategory(null);
        }

        // If primaryImageUrl is provided in update, update or create it
        if (hasText(request.getPrimaryImageUrl())) {
            String newPrimaryUrl = request.getPrimaryImageUrl().trim();
            TourImage currentPrimary = tour.getPrimaryImage();
            if (currentPrimary != null) {
                currentPrimary.setImageUrl(newPrimaryUrl);
            } else {
                TourImage newPrimary = TourImage.builder()
                        .imageUrl(newPrimaryUrl)
                        .isPrimary(true)
                        .tour(tour)
                        .build();
                tour.addImage(newPrimary);
                tour.setPrimaryImage(newPrimary);
            }
        }

        Tour updatedTour = tourRepository.save(tour);
        return tourMapper.toResponse(updatedTour);
    }

    @Override
    @Transactional
    public void deleteTour(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour", "id", id));

        try {
            // Null out circular foreign key reference before deleting images and tour
            tour.setPrimaryImage(null);
            tourRepository.saveAndFlush(tour);

            tourRepository.delete(tour);
            tourRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new BadRequestException("Cannot delete tour with id: " + id +
                    " because it is referenced by existing bookings or schedules");
        }
    }

    @Override
    @Transactional
    public TourResponse addImageToTour(Long tourId, String imageUrl, boolean isPrimary) {
        if (!hasText(imageUrl)) {
            throw new BadRequestException("Image URL cannot be empty");
        }

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour", "id", tourId));

        if (isPrimary && tour.getImages() != null) {
            tour.getImages().forEach(img -> img.setIsPrimary(false));
        }

        TourImage newImage = TourImage.builder()
                .imageUrl(imageUrl.trim())
                .isPrimary(isPrimary)
                .tour(tour)
                .build();

        tour.addImage(newImage);

        if (isPrimary || tour.getPrimaryImage() == null) {
            tour.setPrimaryImage(newImage);
        }

        Tour saved = tourRepository.save(tour);
        return tourMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void removeImageFromTour(Long tourId, Long imageId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour", "id", tourId));

        TourImage imageToRemove = tourImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("TourImage", "id", imageId));

        if (!imageToRemove.getTour().getTourId().equals(tourId)) {
            throw new BadRequestException("Image with id " + imageId + " does not belong to tour with id " + tourId);
        }

        if (tour.getPrimaryImage() != null && tour.getPrimaryImage().getTourImageId().equals(imageId)) {
            tour.setPrimaryImage(null);
            tourRepository.saveAndFlush(tour);
        }

        tour.removeImage(imageToRemove);
        tourImageRepository.delete(imageToRemove);
    }

    @Override
    @Transactional
    public TourResponse setPrimaryImage(Long tourId, Long imageId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour", "id", tourId));

        TourImage image = tourImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("TourImage", "id", imageId));

        if (!image.getTour().getTourId().equals(tourId)) {
            throw new BadRequestException("Image with id " + imageId + " does not belong to tour with id " + tourId);
        }

        if (tour.getImages() != null) {
            tour.getImages().forEach(img -> img.setIsPrimary(img.getTourImageId().equals(imageId)));
        }

        tour.setPrimaryImage(image);
        Tour saved = tourRepository.save(tour);
        return tourMapper.toResponse(saved);
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private Specification<Tour> addSpecification(
            Specification<Tour> spec,
            Specification<Tour> newSpec) {
        return spec == null ? newSpec : spec.and(newSpec);
    }
}
