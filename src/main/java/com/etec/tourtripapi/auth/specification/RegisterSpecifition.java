package com.etec.tourtripapi.auth.specification;

import org.springframework.data.jpa.domain.Specification;

import com.etec.tourtripapi.auth.entity.User;

public final class RegisterSpecifition {
	private RegisterSpecifition() {
	}

	public static Specification<User> search(String keyword) {
		return (root, query, criteriaBuilder) -> {
			if (keyword == null || keyword.isBlank()) {
				return criteriaBuilder.conjunction();
			}

			String search = "%" + keyword.trim().toLowerCase() + "%";
			return criteriaBuilder.or(
					criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), search),
					criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), search),
					criteriaBuilder.like(criteriaBuilder.lower(root.get("role")), search),
					criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), search));
		};
	}
}
