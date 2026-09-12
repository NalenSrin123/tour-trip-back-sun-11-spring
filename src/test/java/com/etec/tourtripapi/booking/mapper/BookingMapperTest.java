package com.etec.tourtripapi.booking.mapper;

import com.etec.tourtripapi.booking.dto.request.BookingRequest;
import com.etec.tourtripapi.booking.dto.response.BookingResponse;
import com.etec.tourtripapi.booking.entity.Booking;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    private final BookingMapper bookingMapper = new BookingMapper();

    @Test
    void toEntityMapsRequestFieldsAndDefaults() {
        BookingRequest request = BookingRequest.builder()
                .userId(10L)
                .scheduleId(20L)
                .totalAmount(new BigDecimal("299.99"))
                .build();

        Booking booking = bookingMapper.toEntity(request);

        assertNotNull(booking);
        assertNull(booking.getBookingId());
        assertEquals(10L, booking.getUserId());
        assertEquals(20L, booking.getScheduleId());
        assertEquals(new BigDecimal("299.99"), booking.getTotalAmount());
        assertNotNull(booking.getBookingNumber());
        assertTrue(booking.getBookingNumber().startsWith("BK-"));
        assertEquals("PENDING", booking.getBookingStatus());
    }

    @Test
    void toResponseMapsEntityFieldsAndBaseDates() {
        LocalDateTime now = LocalDateTime.now();
        Booking booking = new Booking(1L, 10L, 20L, "BK-TEST01", new BigDecimal("199.50"), "CONFIRMED");
        booking.setCreatedAt(now);
        booking.setUpdatedAt(now);

        BookingResponse response = bookingMapper.toResponse(booking);

        assertNotNull(response);
        assertEquals(1L, response.getBookingId());
        assertEquals(10L, response.getUserId());
        assertEquals(20L, response.getScheduleId());
        assertEquals("BK-TEST01", response.getBookingNumber());
        assertEquals(new BigDecimal("199.50"), response.getTotalAmount());
        assertEquals("CONFIRMED", response.getBookingStatus());
        assertEquals(now, response.getCreatedAt());
        assertEquals(now, response.getUpdatedAt());
    }

    @Test
    void toResponseListMapsAllItems() {
        Booking booking1 = new Booking(1L, 10L, 20L, "BK-01", new BigDecimal("100.00"), "PENDING");
        Booking booking2 = new Booking(2L, 11L, 21L, "BK-02", new BigDecimal("200.00"), "CONFIRMED");

        List<BookingResponse> responses = bookingMapper.toResponseList(List.of(booking1, booking2));

        assertEquals(2, responses.size());
        assertEquals("BK-01", responses.get(0).getBookingNumber());
        assertEquals("BK-02", responses.get(1).getBookingNumber());
    }
}
