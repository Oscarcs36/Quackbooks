package com.ds.quackbooks.controllers;

import org.springframework.web.multipart.MultipartFile;

import com.ds.quackbooks.payload.BookDTO;
import com.ds.quackbooks.payload.BookResponse;
import com.ds.quackbooks.services.BookService;

import jakarta.validation.Valid;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
public class BookController {
    @Autowired
    private BookService service;

    @PostMapping("/admin/categories/{categoryId}/book")
    public ResponseEntity<?> addBook(@Valid @RequestBody BookDTO bookDTO, @PathVariable Long categoryId) {
        BookDTO bookSaved =  service.addBook(categoryId, bookDTO);

        return new ResponseEntity<>(bookSaved, HttpStatus.CREATED);
    }
    
    @GetMapping("/public/books")
    public ResponseEntity<?> getAllBooks() {
        BookResponse bookResponse = service.getAllBooks();

        return new ResponseEntity<>(bookResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/books")
    public ResponseEntity<?> getBooksByCategory(@PathVariable Long categoryId) {
        BookResponse bookResponse = service.searchByCategory(categoryId);
        return new ResponseEntity<>(bookResponse, HttpStatus.OK);
    }

    @GetMapping("/public/books/keyword/{keyword}")
    public ResponseEntity<?> getBooksByKeyword(@PathVariable String keyword) {
        BookResponse bookResponse = service.searchBookByKeyword(keyword);
        return new ResponseEntity<>(bookResponse, HttpStatus.OK);
    }

    @PutMapping("/admin/books/{id}")
    public ResponseEntity<?> updateBook(@Valid @RequestBody BookDTO bookDTO, @PathVariable Long id) {
        BookDTO updateBook = service.updateBook(bookDTO, id);
        return new ResponseEntity<>(updateBook, HttpStatus.OK);
    }

    @DeleteMapping("/admin/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        BookDTO deleteBook = service.deleteBook(id);
        return new ResponseEntity<>(deleteBook, HttpStatus.OK);
    }

    @PutMapping("/admin/books/{id}/image")
    public ResponseEntity<?> updateImage(@PathVariable Long id, @RequestParam("image")MultipartFile image) throws IOException {
        BookDTO bookUpdated = service.updateBookImage(id, image);
        
        return new ResponseEntity<>(bookUpdated, HttpStatus.OK);
    }
    
}
