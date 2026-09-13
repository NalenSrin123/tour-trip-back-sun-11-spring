package com.etec.tourtripapi.schedule.controller;

import com.etec.tourtripapi.common.response.ApiResponse;
import com.etec.tourtripapi.schedule.dto.request.TourScheduleRequest;
import com.etec.tourtripapi.schedule.dto.response.TourScheduleResponse;
import com.etec.tourtripapi.schedule.exception.ScheduleConflictException;
import com.etec.tourtripapi.schedule.exception.ScheduleValidationException;
import com.etec.tourtripapi.schedule.service.TourScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class TourScheduleController {

    private final TourScheduleService tourScheduleService;

    /**
     * POST /api/v1/schedules
     * Creates a new tour schedule.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TourScheduleResponse>> createSchedule(
            @Valid @RequestBody TourScheduleRequest request) {
        TourScheduleResponse response = tourScheduleService.createSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tour schedule created successfully", response));
    }

    /**
     * GET /api/v1/schedules
     * Retrieves all tour schedules, optionally filtered by tourId or guideId.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TourScheduleResponse>>> getAllSchedules(
            @RequestParam(required = false) Long tourId,
            @RequestParam(required = false) Long guideId) {
        List<TourScheduleResponse> schedules;
        if (tourId != null) {
            schedules = tourScheduleService.getSchedulesByTourId(tourId);
        } else if (guideId != null) {
            schedules = tourScheduleService.getSchedulesByGuideId(guideId);
        } else {
            schedules = tourScheduleService.getAllSchedules();
        }
        return ResponseEntity.ok(ApiResponse.success("Tour schedules retrieved successfully", schedules));
    }

    /**
     * GET /api/v1/schedules/{id}
     * Retrieves a tour schedule by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TourScheduleResponse>> getScheduleById(@PathVariable Long id) {
        TourScheduleResponse response = tourScheduleService.getScheduleById(id);
        return ResponseEntity.ok(ApiResponse.success("Tour schedule retrieved successfully", response));
    }

    /**
     * PUT /api/v1/schedules/{id}
     * Updates an existing tour schedule.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TourScheduleResponse>> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody TourScheduleRequest request) {
        TourScheduleResponse response = tourScheduleService.updateSchedule(id, request);
        return ResponseEntity.ok(ApiResponse.success("Tour schedule updated successfully", response));
    }

    /**
     * DELETE /api/v1/schedules/{id}
     * Deletes a tour schedule by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(@PathVariable Long id) {
        tourScheduleService.deleteSchedule(id);
        return ResponseEntity.ok(ApiResponse.success("Tour schedule deleted successfully"));
    }

    @ExceptionHandler(ScheduleValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(ScheduleValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(ScheduleConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflictException(ScheduleConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(HttpStatus.CONFLICT.value(), "Operation failed due to database integrity constraints."));
    }
}
