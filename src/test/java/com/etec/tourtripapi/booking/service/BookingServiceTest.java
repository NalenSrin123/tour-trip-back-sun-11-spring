package com.etec.tourtripapi.booking.service;

import com.etec.tourtripapi.booking.dto.request.BookingRequest;
import com.etec.tourtripapi.booking.dto.response.BookingResponse;
import com.etec.tourtripapi.booking.entity.Booking;
import com.etec.tourtripapi.booking.mapper.BookingMapper;
import com.etec.tourtripapi.booking.repository.BookingRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final BookingMapper bookingMapper = new BookingMapper();
    private final BookingService bookingService = new BookingService(bookingRepository, bookingMapper);

    @Test
    void getAllBookingsReturnsResponseDtos() {
        when(bookingRepository.findAll()).thenReturn(List.of(
                new Booking(1L, 10L, 20L, "BK-01", new BigDecimal("100.00"), "PENDING")
        ));

        List<BookingResponse> responses = bookingService.getAllBookings();

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getBookingId());
        assertEquals("BK-01", responses.get(0).getBookingNumber());
    }

    @Test
    void getBookingByIdReturnsResponseWhenFound() {
        Booking booking = new Booking(1L, 10L, 20L, "BK-01", new BigDecimal("100.00"), "PENDING");
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        BookingResponse response = bookingService.getBookingById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getBookingId());
        assertEquals("BK-01", response.getBookingNumber());
    }

    @Test
    void getBookingByIdThrowsWhenNotFound() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookingService.getBookingById(99L));
    }

    @Test
    void createBookingSavesAndReturnsResponse() {
        BookingRequest request = BookingRequest.builder()
                .userId(10L)
                .scheduleId(20L)
                .bookingNumber("BK-CUSTOM")
                .totalAmount(new BigDecimal("250.00"))
                .bookingStatus("PENDING")
                .build();

        when(bookingRepository.existsByBookingNumber("BK-CUSTOM")).thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            b.setBookingId(1L);
            return b;
        });

        BookingResponse response = bookingService.createBooking(request);

        assertNotNull(response);
        assertEquals(1L, response.getBookingId());
        assertEquals("BK-CUSTOM", response.getBookingNumber());
        assertEquals(new BigDecimal("250.00"), response.getTotalAmount());
    }

    @Test
    void createBookingThrowsWhenBookingNumberAlreadyExists() {
        BookingRequest request = BookingRequest.builder()
                .userId(10L)
                .scheduleId(20L)
                .bookingNumber("BK-EXISTS")
                .totalAmount(new BigDecimal("250.00"))
                .build();

        when(bookingRepository.existsByBookingNumber("BK-EXISTS")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(request));
    }

    @Test
    void updateBookingModifiesFieldsAndSaves() {
        Booking existing = new Booking(1L, 10L, 20L, "BK-01", new BigDecimal("100.00"), "PENDING");
        BookingRequest updateReq = BookingRequest.builder()
                .userId(10L)
                .scheduleId(20L)
                .bookingNumber("BK-01")
                .totalAmount(new BigDecimal("300.00"))
                .bookingStatus("CONFIRMED")
                .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(bookingRepository.findByBookingNumber("BK-01")).thenReturn(Optional.of(existing));
        when(bookingRepository.save(existing)).thenReturn(existing);

        BookingResponse response = bookingService.updateBooking(1L, updateReq);

        assertNotNull(response);
        assertEquals(new BigDecimal("300.00"), response.getTotalAmount());
        assertEquals("CONFIRMED", response.getBookingStatus());
        verify(bookingRepository).save(existing);
    }

    @Test
    void deleteBookingRemovesEntity() {
        Booking existing = new Booking(1L, 10L, 20L, "BK-01", new BigDecimal("100.00"), "PENDING");
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(existing));

        bookingService.deleteBooking(1L);

        verify(bookingRepository).delete(existing);
    }
}
