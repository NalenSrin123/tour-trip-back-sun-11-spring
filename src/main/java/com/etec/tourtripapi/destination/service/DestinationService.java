package com.etec.tourtripapi.destination.service;

import com.etec.tourtripapi.destination.dto.request.UpdateDestinationRequest;
import com.etec.tourtripapi.destination.dto.response.DestinationResponse;

public interface DestinationService {

    /**
     * Updates an existing destination by ID.
     *
     * @param id      The ID of the destination to update.
     * @param request Payload containing updated details.
     * @return DestinationResponse of the updated entity.
     */
    DestinationResponse updateDestination(Long id, UpdateDestinationRequest request);

    /**
     * Deletes a destination by ID.
     *
     * @param id The ID of the destination to delete.
     */
    void deleteDestination(Long id);
}
