package com.ds.quackbooks.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ds.quackbooks.Models.Category;
import com.ds.quackbooks.repositories.CategoryRepository;

@Service
public class CategoryServiceJPA implements CategoryService{
    @Autowired
    CategoryRepository repository;

    @Override
    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        repository.save(category);
    }

    @Override
    public String deleteCategory(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));
        
        repository.delete(category);
        return "Category with id: " + id + " deleted succesfully";
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        Category savedCategory = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));

        category.setId(id);
        savedCategory = repository.save(category);
        return savedCategory;
    }

}
