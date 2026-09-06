package com.etec.tourtripapi.category.service;

import com.etec.tourtripapi.category.dto.request.CategoryRequest;
import com.etec.tourtripapi.category.dto.response.CategoryResponse;
import com.etec.tourtripapi.category.entity.Category;
import com.etec.tourtripapi.category.mapper.CategoryMapper;
import com.etec.tourtripapi.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryMapper.toResponseList(categoryRepository.findAll());
    }

    public List<CategoryResponse> searchCategoriesByName(String name) {
        List<Category> categories;
        if (name != null && !name.trim().isEmpty()) {
            categories = categoryRepository.findByNameContainingIgnoreCase(name);
        } else {
            categories = categoryRepository.findAll();
        }
        return categoryMapper.toResponseList(categories);
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return categoryMapper.toResponse(category);
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        categoryMapper.updateEntity(category, request);

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(updatedCategory);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        categoryRepository.delete(category);
    }
}
