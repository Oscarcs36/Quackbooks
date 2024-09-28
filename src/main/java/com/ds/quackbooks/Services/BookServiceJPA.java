package com.ds.quackbooks.Services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ds.quackbooks.Models.Book;
import com.ds.quackbooks.Models.Category;
import com.ds.quackbooks.exceptions.ResourceNotFoundException;
import com.ds.quackbooks.payload.BookDTO;
import com.ds.quackbooks.repositories.BookRepository;
import com.ds.quackbooks.repositories.CategoryRepository;

@Service
public class BookServiceJPA implements BookService{

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BookDTO addBook(Long categoryId, Book book) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        book.setCategory(category);
        Book savedBook = bookRepository.save(book);
        return modelMapper.map(savedBook, BookDTO.class);
    }

}
