package com.etec.tourtripapi.tour.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourImageResponse {

    private Long tourImageId;
    private String imageUrl;
    private Boolean isPrimary;
    private LocalDateTime createdAt;
}
