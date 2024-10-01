package com.ds.quackbooks.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ds.quackbooks.config.AppConstants;
import com.ds.quackbooks.payload.CategoryDTO;
import com.ds.quackbooks.payload.CategoryResponse;
import com.ds.quackbooks.services.CategoryService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    CategoryService service;

    @GetMapping("/public/categories")
    public ResponseEntity<?> getAllCategories(@RequestParam(name = "page", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                              @RequestParam(name = "size", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                              @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
                                              @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        CategoryResponse response = service.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = service.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/public/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
            CategoryDTO deleted = service.deleteCategory(id);
            return new ResponseEntity<>(deleted, HttpStatus.OK);
    }
    
    @PutMapping("/public/categories/{id}")
    public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long id) {
        CategoryDTO savedCategory = service.updateCategory(categoryDTO, id);
        return new ResponseEntity<>(savedCategory, HttpStatus.OK);
    }
}
