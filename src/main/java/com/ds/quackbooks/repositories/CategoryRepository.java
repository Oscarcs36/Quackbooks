package com.ds.quackbooks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ds.quackbooks.Models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    Category findByName(String name);
}
