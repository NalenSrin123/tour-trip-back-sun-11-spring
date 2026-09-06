package com.etec.tourtripapi.category.mapper;

import com.etec.tourtripapi.category.dto.request.CategoryRequest;
import com.etec.tourtripapi.category.dto.response.CategoryResponse;
import com.etec.tourtripapi.category.entity.Category;
import com.etec.tourtripapi.common.mapper.BaseMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper implements BaseMapper<Category, CategoryRequest, CategoryResponse> {

    @Override
    public Category toEntity(CategoryRequest request) {
        if (request == null) {
            return null;
        }

        Category category = new Category();
        updateEntity(category, request);
        return category;
    }

    @Override
    public CategoryResponse toResponse(Category entity) {
        if (entity == null) {
            return null;
        }

        return CategoryResponse.builder()
                .categoryId(entity.getCategoryId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Override
    public void updateEntity(Category entity, CategoryRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
    }
}
