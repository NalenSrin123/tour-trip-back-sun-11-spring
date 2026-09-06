package com.etec.tourtripapi.destination.mapper;

import com.etec.tourtripapi.destination.dto.request.DestinationRequest;
import com.etec.tourtripapi.destination.dto.request.UpdateDestinationRequest;
import com.etec.tourtripapi.destination.dto.response.DestinationResponse;
import com.etec.tourtripapi.destination.entity.Destination;

public class DestinationMapper {

    public static Destination toEntity(DestinationRequest request) {
        if (request == null) {
            return null;
        }

        return Destination.builder()
                .name(request.getName())
                .city(request.getCity())
                .country(request.getCountry())
                .coverImageUrl(request.getCoverImageUrl())
                .build();
    }

    public static DestinationResponse toResponse(Destination destination) {
        if (destination == null) {
            return null;
        }

        return DestinationResponse.builder()
                .id(destination.getDestinationId())
                .name(destination.getName())
                .city(destination.getCity())
                .country(destination.getCountry())
                .coverImageUrl(destination.getCoverImageUrl())
                .createdAt(destination.getCreatedAt())
                .updatedAt(destination.getUpdatedAt())
                .build();
    }

    public static void updateEntityFromRequest(Destination destination, UpdateDestinationRequest request) {
        if (destination == null || request == null) {
            return;
        }

        if (request.getName() != null) {
            destination.setName(request.getName());
        }
        if (request.getCity() != null) {
            destination.setCity(request.getCity());
        }
        if (request.getCountry() != null) {
            destination.setCountry(request.getCountry());
        }
        if (request.getCoverImageUrl() != null) {
            destination.setCoverImageUrl(request.getCoverImageUrl());
        }
    }
}
