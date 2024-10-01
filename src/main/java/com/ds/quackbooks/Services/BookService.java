package com.ds.quackbooks.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ds.quackbooks.payload.BookDTO;
import com.ds.quackbooks.payload.BookResponse;

public interface BookService {
    BookDTO addBook(Long categoryId, BookDTO bookDTO);
    BookResponse getAllBooks();
    BookResponse searchByCategory(Long id);
    BookResponse searchBookByKeyword(String keyword);
    BookDTO updateBook(BookDTO bookDTO, Long id);
    BookDTO deleteBook(Long id);
    BookDTO updateBookImage(Long id, MultipartFile image) throws IOException;
}
