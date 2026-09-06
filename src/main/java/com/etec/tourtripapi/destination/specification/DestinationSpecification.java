package com.etec.tourtripapi.destination.specification;

import com.etec.tourtripapi.destination.entity.Destination;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class DestinationSpecification {

    private DestinationSpecification() {
    }

    public static Specification<Destination> nameContains(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase(Locale.ROOT) + "%"
                );
    }

    public static Specification<Destination> countryEquals(String country) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("country")),
                        country.toLowerCase(Locale.ROOT)
                );
    }

    public static Specification<Destination> cityEquals(String city) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("city")),
                        city.toLowerCase(Locale.ROOT)
                );
    }
}
