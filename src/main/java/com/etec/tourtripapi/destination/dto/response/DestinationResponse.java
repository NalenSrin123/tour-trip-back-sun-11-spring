package com.etec.tourtripapi.destination.dto.response;

import com.etec.tourtripapi.destination.entity.Destination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DestinationResponse {

    private Long id;
    private String name;
    private String city;
    private String country;
    private String coverImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static DestinationResponse fromEntity(Destination destination) {
        if (destination == null) return null;
        return DestinationResponse.builder()
                .id(destination.getId())
                .name(destination.getName())
                .city(destination.getCity())
                .country(destination.getCountry())
                .coverImageUrl(destination.getCoverImageUrl())
                .createdAt(destination.getCreatedAt())
                .updatedAt(destination.getUpdatedAt())
                .build();
    }
}
