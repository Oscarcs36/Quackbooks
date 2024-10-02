package com.ds.quackbooks.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ds.quackbooks.payload.BookDTO;
import com.ds.quackbooks.payload.BookResponse;

public interface BookService {
    BookDTO addBook(Long categoryId, BookDTO bookDTO);
    BookResponse getAllBooks(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    BookResponse searchByCategory(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    BookResponse searchBookByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    BookDTO updateBook(BookDTO bookDTO, Long id);
    BookDTO deleteBook(Long id);
    BookDTO updateBookImage(Long id, MultipartFile image) throws IOException;
}
