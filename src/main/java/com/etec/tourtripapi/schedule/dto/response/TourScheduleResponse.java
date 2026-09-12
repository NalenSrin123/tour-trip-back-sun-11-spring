package com.etec.tourtripapi.schedule.dto.response;

import com.etec.tourtripapi.common.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TourScheduleResponse extends BaseResponse {

    private Long scheduleId;
    private Long tourId;
    private Long guideId;
    private String guideName;
    private LocalDateTime departureDate;
    private LocalDateTime returnDate;
    private Integer maxCapacity;
    private Integer availableSlots;
}
