package com.etec.tourtripapi.guide.specification;

import com.etec.tourtripapi.guide.entity.Guides;
import org.springframework.data.jpa.domain.Specification;

public class GuideSpecification {
    public static Specification<Guides> search(String keyword) {

        return (root, query, criteriaBuilder) -> {

            if (keyword == null || keyword.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String search = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(

                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            search
                    ),

                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("phoneNumber")),
                            search
                    ),

                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("email")),
                            search
                    )
            );
        };
    }
}
