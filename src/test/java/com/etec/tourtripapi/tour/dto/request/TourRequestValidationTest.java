package com.etec.tourtripapi.tour.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TourRequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validRequestHasNoViolations() {
        TourRequest request = TourRequest.builder()
                .title("Angkor Wat Heritage Tour")
                .description("Explore the ancient temples of Angkor")
                .durationDays(3)
                .basePrice(new BigDecimal("150.00"))
                .build();

        Set<ConstraintViolation<TourRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void blankTitleIsRejected() {
        TourRequest request = TourRequest.builder()
                .title("   ")
                .durationDays(3)
                .basePrice(new BigDecimal("150.00"))
                .build();

        Set<ConstraintViolation<TourRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void zeroOrNegativeDurationDaysIsRejected() {
        TourRequest request = TourRequest.builder()
                .title("Island Tour")
                .durationDays(0)
                .basePrice(new BigDecimal("50.00"))
                .build();

        Set<ConstraintViolation<TourRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void negativeBasePriceIsRejected() {
        TourRequest request = TourRequest.builder()
                .title("City Walk")
                .durationDays(1)
                .basePrice(new BigDecimal("-10.00"))
                .build();

        Set<ConstraintViolation<TourRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void oversizedTitleIsRejected() {
        TourRequest request = TourRequest.builder()
                .title("a".repeat(256))
                .durationDays(1)
                .basePrice(new BigDecimal("10.00"))
                .build();

        Set<ConstraintViolation<TourRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }
}
