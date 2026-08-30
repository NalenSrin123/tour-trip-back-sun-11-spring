package com.etec.tourtripapi.destination.service.impl;

import com.etec.tourtripapi.common.exception.ResourceNotFoundException;
import com.etec.tourtripapi.destination.dto.request.DestinationRequest;
import com.etec.tourtripapi.destination.dto.request.UpdateDestinationRequest;
import com.etec.tourtripapi.destination.dto.response.DestinationResponse;
import com.etec.tourtripapi.destination.entity.Destination;
import com.etec.tourtripapi.destination.repository.DestinationRepository;
import com.etec.tourtripapi.destination.service.DestinationService;
import com.etec.tourtripapi.destination.specification.DestinationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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
        return DestinationResponse.fromEntity(destination);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DestinationResponse> searchDestinations(String name, String country, String city) {
        Specification<Destination> specification = null;

        if (hasText(name)) {
            specification = addSpecification(specification, DestinationSpecification.nameContains(name.trim()));
        }

        if (hasText(country)) {
            specification = addSpecification(specification, DestinationSpecification.countryEquals(country.trim()));
        }

        if (hasText(city)) {
            specification = addSpecification(specification, DestinationSpecification.cityEquals(city.trim()));
        }

        List<Destination> destinations = specification == null
                ? destinationRepository.findAll()
                : destinationRepository.findAll(specification);

        return destinations.stream()
                .map(DestinationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DestinationResponse createDestination(DestinationRequest request) {
        Destination destination = Destination.builder()
                .name(request.getName())
                .city(request.getCity())
                .country(request.getCountry())
                .coverImageUrl(request.getCoverImageUrl())
                .build();

        Destination savedDestination = destinationRepository.save(destination);
        return DestinationResponse.fromEntity(savedDestination);
    }

    @Override
    @Transactional
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

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private Specification<Destination> addSpecification(
            Specification<Destination> specification,
            Specification<Destination> newSpecification) {

        return specification == null ? newSpecification : specification.and(newSpecification);
    }
}
