package com.etec.tourtripapi.booking.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingRequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validRequestHasNoViolations() {
        BookingRequest request = BookingRequest.builder()
                .userId(1L)
                .scheduleId(2L)
                .bookingNumber("BK-12345")
                .totalAmount(new BigDecimal("150.00"))
                .bookingStatus("PENDING")
                .build();

        Set<ConstraintViolation<BookingRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void nullUserIdIsRejected() {
        BookingRequest request = BookingRequest.builder()
                .userId(null)
                .scheduleId(2L)
                .totalAmount(new BigDecimal("100.00"))
                .build();

        Set<ConstraintViolation<BookingRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void nullScheduleIdIsRejected() {
        BookingRequest request = BookingRequest.builder()
                .userId(1L)
                .scheduleId(null)
                .totalAmount(new BigDecimal("100.00"))
                .build();

        Set<ConstraintViolation<BookingRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void negativeTotalAmountIsRejected() {
        BookingRequest request = BookingRequest.builder()
                .userId(1L)
                .scheduleId(2L)
                .totalAmount(new BigDecimal("-10.00"))
                .build();

        Set<ConstraintViolation<BookingRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void oversizedFieldsAreRejected() {
        BookingRequest request = BookingRequest.builder()
                .userId(1L)
                .scheduleId(2L)
                .bookingNumber("a".repeat(101))
                .totalAmount(new BigDecimal("100.00"))
                .bookingStatus("b".repeat(51))
                .build();

        Set<ConstraintViolation<BookingRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }
}
