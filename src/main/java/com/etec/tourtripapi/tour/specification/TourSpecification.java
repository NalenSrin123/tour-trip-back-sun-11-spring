package com.etec.tourtripapi.tour.specification;

import com.etec.tourtripapi.tour.entity.Tour;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Locale;

public class TourSpecification {

    private TourSpecification() {
    }

    public static Specification<Tour> keywordContains(String keyword) {
        return (root, query, cb) -> {
            String pattern = "%" + keyword.toLowerCase(Locale.ROOT) + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)
            );
        };
    }

    public static Specification<Tour> categoryEquals(Long categoryId) {
        return (root, query, cb) -> cb.equal(root.get("category").get("categoryId"), categoryId);
    }

    public static Specification<Tour> priceGreaterThanOrEqualTo(BigDecimal minPrice) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("basePrice"), minPrice);
    }

    public static Specification<Tour> priceLessThanOrEqualTo(BigDecimal maxPrice) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("basePrice"), maxPrice);
    }

    public static Specification<Tour> durationDaysEquals(Integer durationDays) {
        return (root, query, cb) -> cb.equal(root.get("durationDays"), durationDays);
    }
}
