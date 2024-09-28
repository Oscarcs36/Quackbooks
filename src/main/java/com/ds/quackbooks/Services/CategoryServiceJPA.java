package com.ds.quackbooks.Services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ds.quackbooks.Models.Category;
import com.ds.quackbooks.exceptions.APIException;
import com.ds.quackbooks.exceptions.ResourceNotFoundException;
import com.ds.quackbooks.payload.CategoryDTO;
import com.ds.quackbooks.payload.CategoryResponse;
import com.ds.quackbooks.repositories.CategoryRepository;

@Service
public class CategoryServiceJPA implements CategoryService{
    @Autowired
    CategoryRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equals("asc") ? 
                    Sort.by(sortBy).ascending() : 
                    Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> page = repository.findAll(pageDetails);

        List<Category> categories = page.getContent();
        if(categories.isEmpty())
            throw new APIException("No categories created");

        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOs);
        categoryResponse.setPageNumber(page.getNumber());
        categoryResponse.setPageSize(page.getSize());
        categoryResponse.setTotalElements(page.getTotalElements());
        categoryResponse.setTotalPages(page.getTotalPages());
        categoryResponse.setLastPage(page.isLast());
        
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category checkCategory = repository.findByName(category.getName());
        if(checkCategory != null){
            throw new APIException("Category with name: " + category.getName() + " already exists");
        }

        Category savedCategory = repository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);        
    }

    @Override
    public CategoryDTO deleteCategory(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        
        repository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id) {
        Category savedCategory = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setId(id);
        savedCategory = repository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

}
