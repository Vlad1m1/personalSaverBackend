package com.vlad1m1.personal.service;

import com.vlad1m1.personal.dto.MemoCategoryResponse;
import com.vlad1m1.personal.mapper.ApiMapper;
import com.vlad1m1.personal.model.Category;
import com.vlad1m1.personal.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
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
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public Category createCategory(Category category) {
        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена: " + id));
        category.setName(categoryDetails.getName());
        category.setIconName(categoryDetails.getIconName());
        category.setDisplayOrder(categoryDetails.getDisplayOrder());
        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
