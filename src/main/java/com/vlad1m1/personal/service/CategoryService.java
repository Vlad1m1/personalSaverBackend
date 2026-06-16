package com.vlad1m1.personal.service;

import com.vlad1m1.personal.dto.MemoCategoryRequest;
import com.vlad1m1.personal.dto.MemoCategoryResponse;
import com.vlad1m1.personal.exception.ResourceNotFoundException;
import com.vlad1m1.personal.mapper.ApiMapper;
import com.vlad1m1.personal.model.Category;
import com.vlad1m1.personal.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<MemoCategoryResponse> getAllCategoryResponses() {
        return categoryRepository.findAll().stream()
                .map(ApiMapper::toMemoCategoryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MemoCategoryResponse getCategoryResponseById(Long id) {
        return categoryRepository.findById(id)
                .map(ApiMapper::toMemoCategoryResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public MemoCategoryResponse createCategory(MemoCategoryRequest request) {
        Category category = new Category();
        apply(category, request);
        category.setUpdatedAt(LocalDateTime.now());
        return ApiMapper.toMemoCategoryResponse(categoryRepository.save(category));
    }

    @Transactional
    public Category createCategory(Category category) {
        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    @Transactional
    public MemoCategoryResponse updateCategory(Long id, MemoCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
        apply(category, request);
        category.setUpdatedAt(LocalDateTime.now());
        return ApiMapper.toMemoCategoryResponse(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found: " + id);
        }
        categoryRepository.deleteById(id);
    }

    private void apply(Category category, MemoCategoryRequest request) {
        category.setName(request.name().trim());
        category.setIconName(blankToNull(request.iconName()));
        category.setAccentColor(blankToNull(request.accentColor()));
        category.setDisplayOrder(request.displayOrder() == null ? 0 : request.displayOrder());
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
