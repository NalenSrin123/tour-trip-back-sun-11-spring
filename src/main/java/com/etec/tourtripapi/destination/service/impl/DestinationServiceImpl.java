package com.etec.tourtripapi.destination.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etec.tourtripapi.common.exception.ResourceNotFoundException;
import com.etec.tourtripapi.destination.dto.request.UpdateDestinationRequest;
import com.etec.tourtripapi.destination.dto.response.DestinationResponse;
import com.etec.tourtripapi.destination.entity.Destination;
import com.etec.tourtripapi.destination.repository.DestinationRepository;
import com.etec.tourtripapi.destination.service.DestinationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository destinationRepository;

    @Override
    @Transactional//This is annotation for transaction management
    public DestinationResponse updateDestination(Long id, UpdateDestinationRequest request) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination", "id", id));

        destination.setName(request.getName());
        destination.setCity(request.getCity());
        destination.setCountry(request.getCountry());
        destination.setCoverImageUrl(request.getCoverImageUrl());

        Destination updatedDestination = destinationRepository.save(destination);
        return DestinationResponse.fromEntity(updatedDestination);
    }

    @Override
    @Transactional
    public void deleteDestination(Long id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination", "id", id));

        destinationRepository.delete(destination);
    }
}
