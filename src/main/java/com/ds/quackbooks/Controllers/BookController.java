package com.ds.quackbooks.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ds.quackbooks.Models.Book;
import com.ds.quackbooks.Services.BookService;
import com.ds.quackbooks.payload.BookDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
public class BookController {
    @Autowired
    private BookService service;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<?> addBook(@RequestBody Book book, @PathVariable Long categoryId) {
        BookDTO bookDTO =  service.addBook(categoryId, book);

        return new ResponseEntity<>(bookDTO, HttpStatus.CREATED);
    }
    
}
