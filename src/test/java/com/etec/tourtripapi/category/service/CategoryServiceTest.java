package com.etec.tourtripapi.category.service;

import com.etec.tourtripapi.category.dto.request.CategoryRequest;
import com.etec.tourtripapi.category.dto.response.CategoryResponse;
import com.etec.tourtripapi.category.entity.Category;
import com.etec.tourtripapi.category.mapper.CategoryMapper;
import com.etec.tourtripapi.category.repository.CategoryRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategoryServiceTest {

    private final CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private final CategoryMapper categoryMapper = new CategoryMapper();
    private final CategoryService categoryService = new CategoryService(categoryRepository, categoryMapper);

    @Test
    void getAllCategoriesReturnsResponseDtos() {
        when(categoryRepository.findAll()).thenReturn(List.of(
                new Category(1L, "Beach", "Sea trips")
        ));

        List<CategoryResponse> responses = categoryService.getAllCategories();

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getCategoryId());
        assertEquals("Beach", responses.get(0).getName());
    }

    @Test
    void searchCategoriesByNameUsesRepositorySearchWhenNameProvided() {
        when(categoryRepository.findByNameContainingIgnoreCase("beach")).thenReturn(List.of(
                new Category(1L, "Beach", "Sea trips")
        ));

        List<CategoryResponse> responses = categoryService.searchCategoriesByName("beach");

        assertEquals(1, responses.size());
        assertEquals("Beach", responses.get(0).getName());
        verify(categoryRepository).findByNameContainingIgnoreCase("beach");
    }

    @Test
    void createCategorySavesMappedEntityAndReturnsResponse() {
        CategoryRequest request = new CategoryRequest("Adventure", "Outdoor trips");
        Category savedCategory = new Category(1L, "Adventure", "Outdoor trips");
        when(categoryRepository.save(org.mockito.ArgumentMatchers.any(Category.class))).thenReturn(savedCategory);

        CategoryResponse response = categoryService.createCategory(request);

        assertEquals(1L, response.getCategoryId());
        assertEquals("Adventure", response.getName());
        assertEquals("Outdoor trips", response.getDescription());
    }

    @Test
    void updateCategoryChangesNameAndDescription() {
        Category existingCategory = new Category(1L, "Old", "Old description");
        CategoryRequest request = new CategoryRequest("New", "New description");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(existingCategory);

        CategoryResponse response = categoryService.updateCategory(1L, request);

        assertEquals("New", response.getName());
        assertEquals("New description", response.getDescription());
        verify(categoryRepository).save(existingCategory);
    }

    @Test
    void updateCategoryThrowsWhenMissing() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoryService.updateCategory(99L, new CategoryRequest("New", null)));
    }
}
