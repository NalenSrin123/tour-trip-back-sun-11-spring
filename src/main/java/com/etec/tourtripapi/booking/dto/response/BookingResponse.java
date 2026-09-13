package com.etec.tourtripapi.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private Long userId;
    private Long scheduleId;
    private String bookingNumber;
    private BigDecimal totalAmount;
    private String bookingStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
