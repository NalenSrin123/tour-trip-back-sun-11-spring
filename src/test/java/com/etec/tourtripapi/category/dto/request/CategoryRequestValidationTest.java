package com.etec.tourtripapi.category.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryRequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validRequestHasNoViolations() {
        CategoryRequest request = new CategoryRequest("Adventure", "Outdoor trips");

        Set<ConstraintViolation<CategoryRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void blankNameIsRejected() {
        CategoryRequest request = new CategoryRequest(" ", "Outdoor trips");

        Set<ConstraintViolation<CategoryRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void oversizedFieldsAreRejected() {
        CategoryRequest request = new CategoryRequest("a".repeat(101), "b".repeat(256));

        Set<ConstraintViolation<CategoryRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }
}
