package com.ds.quackbooks.controllers;

import org.springframework.web.multipart.MultipartFile;

import com.ds.quackbooks.config.AppConstants;
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
    public ResponseEntity<?> getAllBooks(@RequestParam(name = "page", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                         @RequestParam(name = "size", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                         @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BOOKS_BY, required = false) String sortBy,
                                         @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        BookResponse bookResponse = service.getAllBooks(pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<>(bookResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/books")
    public ResponseEntity<?> getBooksByCategory(@PathVariable Long categoryId,
                                                @RequestParam(name = "page", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                @RequestParam(name = "size", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BOOKS_BY, required = false) String sortBy,
                                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        BookResponse bookResponse = service.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(bookResponse, HttpStatus.OK);
    }

    @GetMapping("/public/books/keyword/{keyword}")
    public ResponseEntity<?> getBooksByKeyword(@PathVariable String keyword,
                                                @RequestParam(name = "page", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                @RequestParam(name = "size", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BOOKS_BY, required = false) String sortBy,
                                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        BookResponse bookResponse = service.searchBookByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
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
