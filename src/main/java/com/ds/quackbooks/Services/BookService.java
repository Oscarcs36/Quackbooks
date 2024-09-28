package com.ds.quackbooks.Services;

import com.ds.quackbooks.Models.Book;
import com.ds.quackbooks.payload.BookDTO;

public interface BookService {
    BookDTO addBook(Long categoryId, Book book);
}
