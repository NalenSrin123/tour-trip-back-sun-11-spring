package com.etec.tourtripapi.booking.service;

import com.etec.tourtripapi.booking.dto.request.BookingRequest;
import com.etec.tourtripapi.booking.dto.response.BookingResponse;
import com.etec.tourtripapi.booking.entity.Booking;
import com.etec.tourtripapi.booking.mapper.BookingMapper;
import com.etec.tourtripapi.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public BookingService(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    public List<BookingResponse> getAllBookings() {
        return bookingMapper.toResponseList(bookingRepository.findAll());
    }

    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        return bookingMapper.toResponse(booking);
    }

    public BookingResponse getBookingByNumber(String bookingNumber) {
        Booking booking = bookingRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new RuntimeException("Booking not found with booking number: " + bookingNumber));
        return bookingMapper.toResponse(booking);
    }

    public List<BookingResponse> getBookingsByUserId(Long userId) {
        return bookingMapper.toResponseList(bookingRepository.findByUserId(userId));
    }

    public List<BookingResponse> getBookingsByStatus(String status) {
        if (status != null && !status.trim().isEmpty()) {
            return bookingMapper.toResponseList(bookingRepository.findByBookingStatusIgnoreCase(status));
        }
        return getAllBookings();
    }

    public BookingResponse createBooking(BookingRequest request) {
        if (request.getBookingNumber() != null && !request.getBookingNumber().trim().isEmpty()) {
            if (bookingRepository.existsByBookingNumber(request.getBookingNumber())) {
                throw new RuntimeException("Booking number already exists: " + request.getBookingNumber());
            }
        }

        Booking booking = bookingMapper.toEntity(request);
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toResponse(savedBooking);
    }

    public BookingResponse updateBooking(Long id, BookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        if (request.getBookingNumber() != null && !request.getBookingNumber().trim().isEmpty()) {
            bookingRepository.findByBookingNumber(request.getBookingNumber())
                    .ifPresent(existing -> {
                        if (!existing.getBookingId().equals(id)) {
                            throw new RuntimeException("Booking number already exists: " + request.getBookingNumber());
                        }
                    });
        }

        bookingMapper.updateEntity(booking, request);
        Booking updatedBooking = bookingRepository.save(booking);
        return bookingMapper.toResponse(updatedBooking);
    }

    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        bookingRepository.delete(booking);
    }
}
