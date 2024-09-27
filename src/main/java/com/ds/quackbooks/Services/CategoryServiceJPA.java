package com.ds.quackbooks.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ds.quackbooks.Models.Category;
import com.ds.quackbooks.exceptions.APIException;
import com.ds.quackbooks.exceptions.ResourceNotFoundException;
import com.ds.quackbooks.repositories.CategoryRepository;

@Service
public class CategoryServiceJPA implements CategoryService{
    @Autowired
    CategoryRepository repository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = repository.findAll();
        if(categories.isEmpty())
            throw new APIException("No categories created");
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory = repository.findByName(category.getName());
        if(savedCategory != null){
            throw new APIException("Category with name: " + category.getName() + " already exists");
        }

        repository.save(category);
    }

    @Override
    public String deleteCategory(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        
        repository.delete(category);
        return "Category with id: " + id + " deleted succesfully";
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        Category savedCategory = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        category.setId(id);
        savedCategory = repository.save(category);
        return savedCategory;
    }

}
