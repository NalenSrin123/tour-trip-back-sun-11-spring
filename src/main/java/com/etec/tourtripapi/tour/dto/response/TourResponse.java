package com.etec.tourtripapi.tour.dto.response;

import com.etec.tourtripapi.common.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TourResponse extends BaseResponse {

    private Long tourId;
    private String title;
    private String description;
    private Integer durationDays;
    private BigDecimal basePrice;
    private Long categoryId;
    private String categoryName;
    private String primaryImageUrl;
    private List<TourImageResponse> images;
    private Integer scheduleCount;
}
