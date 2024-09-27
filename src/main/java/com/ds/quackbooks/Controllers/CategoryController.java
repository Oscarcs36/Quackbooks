package com.ds.quackbooks.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ds.quackbooks.Models.Category;
import com.ds.quackbooks.Services.CategoryService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    CategoryService service;

    @GetMapping("/public/categories")
    public ResponseEntity<?> getAllCategories(){
        List<Category> categories = service.getAllCategories();

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        service.createCategory(category);
        return new ResponseEntity<>("Category added succesfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/public/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        try{
            String status = service.deleteCategory(id);

            return ResponseEntity.status(HttpStatus.OK).body(status);
        }catch(ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
    
    @PutMapping("/public/categories/{id}")
    public ResponseEntity<?> updateCategory(@RequestBody Category category, @PathVariable Long id) {
        try{
            service.updateCategory(category, id);
            return new ResponseEntity<>("Category with category id: " + id, HttpStatus.OK);
        } catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}
