package com.etec.tourtripapi.user.specification;

import com.etec.tourtripapi.user.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class UserSpecification {

    private UserSpecification() {
    }

    public static Specification<User> roleEquals(String role) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("role")),
                        role.toLowerCase(Locale.ROOT)
                );
    }

    public static Specification<User> nameContains(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("fullName")),
                        "%" + name.toLowerCase(Locale.ROOT) + "%"
                );
    }

    public static Specification<User> emailContains(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        "%" + email.toLowerCase(Locale.ROOT) + "%"
                );
    }

    public static Specification<User> statusEquals(String status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("status")),
                        status.toLowerCase(Locale.ROOT)
                );
    }
}
