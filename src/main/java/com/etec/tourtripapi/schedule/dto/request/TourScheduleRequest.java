package com.etec.tourtripapi.schedule.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourScheduleRequest {

    @NotNull(message = "Tour ID is required")
    private Long tourId;

    private Long guideId;

    @NotNull(message = "Departure date is required")
    @FutureOrPresent(message = "Departure date must be in the present or future")
    private LocalDateTime departureDate;

    @NotNull(message = "Return date is required")
    private LocalDateTime returnDate;

    @NotNull(message = "Max capacity is required")
    @Min(value = 1, message = "Max capacity must be at least 1")
    private Integer maxCapacity;

    @Min(value = 0, message = "Available slots cannot be negative")
    private Integer availableSlots;
}
