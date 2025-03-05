package com.techie.microservices.cat.service;

import com.techie.microservices.cat.model.Category;
import com.techie.microservices.cat.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // Получить список всех категорий
    public List<Category> listAll() {
        return categoryRepository.findAll();
    }

    // Сохранить или обновить категорию
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    // Удалить категорию по ID
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}