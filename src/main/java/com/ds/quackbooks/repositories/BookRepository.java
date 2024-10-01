package com.ds.quackbooks.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ds.quackbooks.models.Book;
import com.ds.quackbooks.models.Category;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
    List<Book> findByCategoryOrderByPriceAsc(Category category);

    List<Book> findByNameLikeIgnoreCase(String keyword);
}
