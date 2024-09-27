package com.ds.quackbooks.Services;

import java.util.List;

import com.ds.quackbooks.Models.Category;

public interface CategoryService {
    List<Category> getAllCategories();
    void createCategory(Category category);
    String deleteCategory(Long id);
    Category updateCategory(Category category, Long id);
}
