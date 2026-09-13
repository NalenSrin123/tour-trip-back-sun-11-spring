package com.etec.tourtripapi.booking.mapper;

import com.etec.tourtripapi.booking.dto.request.BookingRequest;
import com.etec.tourtripapi.booking.dto.response.BookingResponse;
import com.etec.tourtripapi.booking.entity.Booking;
import com.etec.tourtripapi.common.mapper.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BookingMapper implements BaseMapper<Booking, BookingRequest, BookingResponse> {

    @Override
    public Booking toEntity(BookingRequest request) {
        if (request == null) {
            return null;
        }

        Booking booking = new Booking();
        updateEntity(booking, request);

        if (booking.getBookingNumber() == null || booking.getBookingNumber().trim().isEmpty()) {
            booking.setBookingNumber("BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        if (booking.getBookingStatus() == null || booking.getBookingStatus().trim().isEmpty()) {
            booking.setBookingStatus("PENDING");
        }

        return booking;
    }

    @Override
    public BookingResponse toResponse(Booking entity) {
        if (entity == null) {
            return null;
        }

        return BookingResponse.builder()
                .bookingId(entity.getBookingId())
                .userId(entity.getUserId())
                .scheduleId(entity.getScheduleId())
                .bookingNumber(entity.getBookingNumber())
                .totalAmount(entity.getTotalAmount())
                .bookingStatus(entity.getBookingStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Override
    public void updateEntity(Booking entity, BookingRequest request) {
        if (entity == null || request == null) {
            return;
        }

        if (request.getUserId() != null) {
            entity.setUserId(request.getUserId());
        }
        if (request.getScheduleId() != null) {
            entity.setScheduleId(request.getScheduleId());
        }
        if (request.getBookingNumber() != null && !request.getBookingNumber().trim().isEmpty()) {
            entity.setBookingNumber(request.getBookingNumber());
        }
        if (request.getTotalAmount() != null) {
            entity.setTotalAmount(request.getTotalAmount());
        }
        if (request.getBookingStatus() != null && !request.getBookingStatus().trim().isEmpty()) {
            entity.setBookingStatus(request.getBookingStatus());
        }
    }
}
