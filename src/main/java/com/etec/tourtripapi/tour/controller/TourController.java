package com.etec.tourtripapi.tour.controller;

import com.etec.tourtripapi.common.response.ApiResponse;
import com.etec.tourtripapi.schedule.dto.response.TourScheduleResponse;
import com.etec.tourtripapi.schedule.service.TourScheduleService;
import com.etec.tourtripapi.tour.dto.request.TourRequest;
import com.etec.tourtripapi.tour.dto.response.TourResponse;
import com.etec.tourtripapi.tour.service.TourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tours")
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;
    private final TourScheduleService tourScheduleService;

    /**
     * POST /api/v1/tours
     * Creates a new tour.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TourResponse>> createTour(
            @Valid @RequestBody TourRequest request) {
        TourResponse response = tourService.createTour(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tour created successfully", response));
    }

    /**
     * GET /api/v1/tours
     * Retrieves all tours or searches with optional filters.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TourResponse>>> getAllTours(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer durationDays) {
        List<TourResponse> tours = tourService.searchTours(keyword, categoryId, minPrice, maxPrice, durationDays);
        return ResponseEntity.ok(ApiResponse.success("Tours retrieved successfully", tours));
    }

    /**
     * GET /api/v1/tours/{id}
     * Retrieves a tour by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TourResponse>> getTourById(@PathVariable Long id) {
        TourResponse tour = tourService.getTourById(id);
        return ResponseEntity.ok(ApiResponse.success("Tour retrieved successfully", tour));
    }

    /**
     * PUT /api/v1/tours/{id}
     * Updates an existing tour.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TourResponse>> updateTour(
            @PathVariable Long id,
            @Valid @RequestBody TourRequest request) {
        TourResponse response = tourService.updateTour(id, request);
        return ResponseEntity.ok(ApiResponse.success("Tour updated successfully", response));
    }

    /**
     * DELETE /api/v1/tours/{id}
     * Deletes a tour by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTour(@PathVariable Long id) {
        tourService.deleteTour(id);
        return ResponseEntity.ok(ApiResponse.success("Tour deleted successfully"));
    }

    /**
     * GET /api/v1/tours/{id}/schedules
     * Retrieves all schedules associated with the given tour ID.
     */
    @GetMapping("/{id}/schedules")
    public ResponseEntity<ApiResponse<List<TourScheduleResponse>>> getSchedulesByTourId(@PathVariable Long id) {
        List<TourScheduleResponse> schedules = tourScheduleService.getSchedulesByTourId(id);
        return ResponseEntity.ok(ApiResponse.success("Tour schedules retrieved successfully", schedules));
    }

    /**
     * POST /api/v1/tours/{id}/images
     * Adds an image to a tour.
     */
    @PostMapping("/{id}/images")
    public ResponseEntity<ApiResponse<TourResponse>> addImageToTour(
            @PathVariable Long id,
            @RequestParam String imageUrl,
            @RequestParam(defaultValue = "false") boolean isPrimary) {
        TourResponse response = tourService.addImageToTour(id, imageUrl, isPrimary);
        return ResponseEntity.ok(ApiResponse.success("Image added to tour successfully", response));
    }

    /**
     * DELETE /api/v1/tours/{id}/images/{imageId}
     * Removes an image from a tour.
     */
    @DeleteMapping("/{id}/images/{imageId}")
    public ResponseEntity<ApiResponse<Void>> removeImageFromTour(
            @PathVariable Long id,
            @PathVariable Long imageId) {
        tourService.removeImageFromTour(id, imageId);
        return ResponseEntity.ok(ApiResponse.success("Image removed from tour successfully"));
    }

    /**
     * PATCH /api/v1/tours/{id}/images/{imageId}/primary
     * Sets an image as the primary image of a tour.
     */
    @PatchMapping("/{id}/images/{imageId}/primary")
    public ResponseEntity<ApiResponse<TourResponse>> setPrimaryImage(
            @PathVariable Long id,
            @PathVariable Long imageId) {
        TourResponse response = tourService.setPrimaryImage(id, imageId);
        return ResponseEntity.ok(ApiResponse.success("Primary image updated successfully", response));
    }
}
