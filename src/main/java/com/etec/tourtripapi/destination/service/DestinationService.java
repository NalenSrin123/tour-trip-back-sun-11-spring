package com.etec.tourtripapi.destination.service;

import com.etec.tourtripapi.destination.dto.request.UpdateDestinationRequest;
import com.etec.tourtripapi.destination.dto.response.DestinationResponse;

import java.util.List;

public interface DestinationService {

    /**
     * Retrieves a destination by ID.
     */
    DestinationResponse getDestinationById(Long id);

    /**
     * Searches or retrieves destinations filtered by name, country, or city.
     */
    List<DestinationResponse> searchDestinations(String name, String country, String city);

    /**
     * Updates an existing destination by ID.
     */
    DestinationResponse updateDestination(Long id, UpdateDestinationRequest request);

    /**
     * Deletes a destination by ID.
     */
    void deleteDestination(Long id);
}
