package com.example.ecommerce.service.impl;

import com.example.ecommerce.entity.Category;
import com.example.ecommerce.exception.ResourceNotFound;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category) {
        Category createdCategory = new Category();
        createdCategory.setName(category.getName());
        createdCategory.setDescription(category.getDescription());

        return categoryRepository.save(createdCategory);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategory(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFound("Category not found with the id: " + id));
    }

    @Override
    public Category updateCategory(UUID id, Category category) {
        Category existCategory = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFound("Category not found with the id: " + id));

        existCategory.setName(category.getName());
        existCategory.setDescription(category.getDescription());

        return categoryRepository.save(existCategory);
    }

    @Override
    public String deleteCategory(UUID id) {

        categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFound("Category not found with the id: " + id));

        categoryRepository.deleteById(id);

        return "Category deleted successfully.";
    }
}
