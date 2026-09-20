package com.etec.tourtripapi.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewStatusUpdateRequest {

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "(?i)^(PENDING|APPROVED|REJECTED)$", message = "Status must be PENDING, APPROVED, or REJECTED")
    private String status;
}
