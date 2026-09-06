package com.etec.tourtripapi.category.mapper;

import com.etec.tourtripapi.category.dto.request.CategoryRequest;
import com.etec.tourtripapi.category.dto.response.CategoryResponse;
import com.etec.tourtripapi.category.entity.Category;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CategoryMapperTest {

    private final CategoryMapper categoryMapper = new CategoryMapper();

    @Test
    void toEntityMapsRequestFieldsOnly() {
        CategoryRequest request = new CategoryRequest("Adventure", "Outdoor trips");

        Category category = categoryMapper.toEntity(request);

        assertNull(category.getCategoryId());
        assertEquals("Adventure", category.getName());
        assertEquals("Outdoor trips", category.getDescription());
    }

    @Test
    void toResponseMapsEntityFieldsAndBaseDates() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 9, 6, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 9, 6, 11, 0);
        Category category = new Category(1L, "Beach", "Sea trips");
        category.setCreatedAt(createdAt);
        category.setUpdatedAt(updatedAt);

        CategoryResponse response = categoryMapper.toResponse(category);

        assertEquals(1L, response.getCategoryId());
        assertEquals("Beach", response.getName());
        assertEquals("Sea trips", response.getDescription());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
    }

    @Test
    void toResponseListMapsAllItems() {
        List<Category> categories = List.of(
                new Category(1L, "Beach", "Sea trips"),
                new Category(2L, "City", "Urban trips")
        );

        List<CategoryResponse> responses = categoryMapper.toResponseList(categories);

        assertEquals(2, responses.size());
        assertEquals("Beach", responses.get(0).getName());
        assertEquals("City", responses.get(1).getName());
    }
}
