package com.ds.quackbooks.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ds.quackbooks.models.Book;
import com.ds.quackbooks.models.Category;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
    Page<Book> findByCategoryOrderByPriceAsc(Category category, Pageable page);

    Page<Book> findByNameLikeIgnoreCase(String keyword, Pageable page);
}
