package com.etec.tourtripapi.booking.repository;

import com.etec.tourtripapi.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingNumber(String bookingNumber);

    List<Booking> findByUserId(Long userId);

    List<Booking> findByScheduleId(Long scheduleId);

    List<Booking> findByBookingStatusIgnoreCase(String bookingStatus);

    boolean existsByBookingNumber(String bookingNumber);
}
