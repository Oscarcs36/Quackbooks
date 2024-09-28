package com.ds.quackbooks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ds.quackbooks.Models.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

}
