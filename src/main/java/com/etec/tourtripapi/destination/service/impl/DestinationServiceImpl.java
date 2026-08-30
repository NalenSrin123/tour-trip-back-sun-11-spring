package com.etec.tourtripapi.destination.service.impl;

import com.etec.tourtripapi.common.exception.ResourceNotFoundException;
import com.etec.tourtripapi.destination.dto.request.UpdateDestinationRequest;
import com.etec.tourtripapi.destination.dto.response.DestinationResponse;
import com.etec.tourtripapi.destination.entity.Destination;
import com.etec.tourtripapi.destination.mapper.DestinationMapper;
import com.etec.tourtripapi.destination.repository.DestinationRepository;
import com.etec.tourtripapi.destination.service.DestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository destinationRepository;

    @Override
    @Transactional(readOnly = true)
    public DestinationResponse getDestinationById(Long id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination", "id", id));
        return DestinationMapper.toResponse(destination);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DestinationResponse> searchDestinations(String name, String country, String city) {
        List<Destination> destinations;

        if (name != null && !name.trim().isEmpty()) {
            destinations = destinationRepository.findByNameContainingIgnoreCase(name.trim());
        } else if (country != null && !country.trim().isEmpty()) {
            destinations = destinationRepository.findByCountryIgnoreCase(country.trim());
        } else if (city != null && !city.trim().isEmpty()) {
            destinations = destinationRepository.findByCityIgnoreCase(city.trim());
        } else {
            destinations = destinationRepository.findAll();
        }

        return destinations.stream()
                .map(DestinationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DestinationResponse updateDestination(Long id, UpdateDestinationRequest request) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination", "id", id));

        DestinationMapper.updateEntityFromRequest(destination, request);

        Destination updatedDestination = destinationRepository.save(destination);
        return DestinationMapper.toResponse(updatedDestination);
    }

    @Override
    @Transactional
    public void deleteDestination(Long id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination", "id", id));

        destinationRepository.delete(destination);
    }
}
