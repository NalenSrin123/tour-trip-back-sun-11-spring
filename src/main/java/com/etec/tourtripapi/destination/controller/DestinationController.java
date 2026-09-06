package com.etec.tourtripapi.destination.controller;

import com.etec.tourtripapi.common.response.ApiResponse;
import com.etec.tourtripapi.destination.dto.request.DestinationRequest;
import com.etec.tourtripapi.destination.dto.request.UpdateDestinationRequest;
import com.etec.tourtripapi.destination.dto.response.DestinationResponse;
import com.etec.tourtripapi.destination.service.DestinationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/api/v1/destinations")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationService destinationService;

    /**
     * GET /api/v1/destinations/{id}
     * Retrieves destination details by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DestinationResponse>> getDestinationById(@PathVariable Long id) {
        DestinationResponse destination = destinationService.getDestinationById(id);
        ApiResponse<DestinationResponse> response = ApiResponse.success(
                "Destination retrieved successfully",
                destination
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/destinations
     * Searches or lists destinations filtered by name, country, or city.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<DestinationResponse>>> searchDestinations(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city) {

        List<DestinationResponse> destinations = destinationService.searchDestinations(name, country, city);
        ApiResponse<List<DestinationResponse>> response = ApiResponse.success(
                "Destinations retrieved successfully",
                destinations
        );
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/v1/destinations
     * Creates a new destination.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<DestinationResponse>> createDestination(
            @Valid @RequestBody DestinationRequest request) {

        DestinationResponse destination = destinationService.createDestination(request);
        ApiResponse<DestinationResponse> response = ApiResponse.success(
                "Destination created successfully",
                destination
        );
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/v1/destinations/{id}
     * Updates destination details.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DestinationResponse>> updateDestination(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDestinationRequest request) {

        DestinationResponse updatedDestination = destinationService.updateDestination(id, request);
        ApiResponse<DestinationResponse> response = ApiResponse.success(
                "Destination updated successfully",
                updatedDestination
        );
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/v1/destinations/{id}
     * Deletes a destination by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDestination(@PathVariable Long id) {
        destinationService.deleteDestination(id);
        ApiResponse<Void> response = ApiResponse.success("Destination deleted successfully");
        return ResponseEntity.ok(response);
    }
}
