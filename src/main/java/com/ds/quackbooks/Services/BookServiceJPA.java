package com.ds.quackbooks.services;

import java.io.IOException;
import java.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ds.quackbooks.exceptions.APIException;
import com.ds.quackbooks.exceptions.ResourceNotFoundException;
import com.ds.quackbooks.models.Book;
import com.ds.quackbooks.models.Category;
import com.ds.quackbooks.payload.BookDTO;
import com.ds.quackbooks.payload.BookResponse;
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

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public BookDTO addBook(Long categoryId, BookDTO bookDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        boolean bookNotExists = true;
        List<Book> books = category.getBooks();

        for (Book book : books) {
            if(book.getName().equals(bookDTO.getName())) {
                bookNotExists = false;
                break;
            }
        }

       if(!bookNotExists){
            throw new APIException("Book already exists!");
       }

        Book book = modelMapper.map(bookDTO, Book.class);
        book.setCategory(category);
        Book savedBook = bookRepository.save(book);
        
        return modelMapper.map(savedBook, BookDTO.class);
    }

    @Override
    public BookResponse getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookDTO> booksDTO = books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .toList();

        if(booksDTO.isEmpty()) throw new APIException("No books exists");

        BookResponse bookResponse = new BookResponse();
        bookResponse.setContent(booksDTO);
        return bookResponse;
    }

    @Override
    public BookResponse searchByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "category_id", categoryId));

        List<Book> books = bookRepository.findByCategoryOrderByPriceAsc(category);

        List<BookDTO> booksDTO = books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .toList();

        BookResponse bookResponse = new BookResponse();
        bookResponse.setContent(booksDTO);
        return bookResponse;
    }

    @Override
    public BookResponse searchBookByKeyword(String keyword) {
        List<Book> books = bookRepository.findByNameLikeIgnoreCase('%' + keyword + '%');

        List<BookDTO> booksDTO = books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .toList();

        BookResponse bookResponse = new BookResponse();
        bookResponse.setContent(booksDTO);
        return bookResponse;
    }

    @Override
    public BookDTO updateBook(BookDTO bookDTO, Long id) {
        Book bookDB = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));

        Book book = modelMapper.map(bookDB, Book.class);

        bookDB.setName(book.getName());
        bookDB.setImage(book.getImage());
        bookDB.setPrice(book.getPrice());
        bookDB.setQuantity(book.getQuantity());
        bookDB.setCategory(book.getCategory());

        Book bookSaved = bookRepository.save(bookDB);

        return modelMapper.map(bookSaved, BookDTO.class);
    }

    @Override
    public BookDTO deleteBook(Long id) {
        Book bookDB = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));

        bookRepository.delete(bookDB);
        return modelMapper.map(bookDB, BookDTO.class);
    }

    @Override
    public BookDTO updateBookImage(Long id, MultipartFile image) throws IOException {
        Book bookDB = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
    
        String fileName = fileService.uploadImage(path, image);
        
        bookDB.setImage(fileName);

        Book updatedBook = bookRepository.save(bookDB);

        return modelMapper.map(updatedBook, BookDTO.class);
    }
}
