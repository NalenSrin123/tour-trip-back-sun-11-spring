package com.etec.tourtripapi.destination.controller;

import com.etec.tourtripapi.common.response.ApiResponse;
import com.etec.tourtripapi.destination.dto.request.UpdateDestinationRequest;
import com.etec.tourtripapi.destination.dto.response.DestinationResponse;
import com.etec.tourtripapi.destination.service.DestinationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/destinations")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationService destinationService;

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
