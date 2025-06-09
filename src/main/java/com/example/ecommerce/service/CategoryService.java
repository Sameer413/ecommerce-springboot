package com.example.ecommerce.service;

import com.example.ecommerce.entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category createCategory(Category category);
    List<Category> getAllCategory();
    Category getCategory(UUID id);
    Category updateCategory(UUID id, Category category);
    String deleteCategory(UUID id);
}
