package com.ds.quackbooks.services;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ds.quackbooks.exceptions.APIException;
import com.ds.quackbooks.exceptions.ResourceNotFoundException;
import com.ds.quackbooks.models.Book;
import com.ds.quackbooks.models.Cart;
import com.ds.quackbooks.models.Category;
import com.ds.quackbooks.payload.BookDTO;
import com.ds.quackbooks.payload.BookResponse;
import com.ds.quackbooks.payload.CartDTO;
import com.ds.quackbooks.repositories.BookRepository;
import com.ds.quackbooks.repositories.CartRepository;
import com.ds.quackbooks.repositories.CategoryRepository;

@Service
public class BookServiceJPA implements BookService{

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

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
    public BookResponse getAllBooks(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equals("asc") 
                ? Sort.by(sortBy).ascending()  
                : Sort.by(sortBy).descending();
        
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Book> page = bookRepository.findAll(pageDetails);

        List<Book> books = page.getContent();

        if(books.isEmpty())
            throw new APIException("No books created");

        List<BookDTO> booksDTO = books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .toList();

        BookResponse bookResponse = new BookResponse();
        bookResponse.setContent(booksDTO);
        bookResponse.setPageNumber(page.getNumber());
        bookResponse.setPageSize(page.getSize());
        bookResponse.setTotalElements(page.getTotalElements());
        bookResponse.setTotalPages(page.getTotalPages());
        bookResponse.setLastPage(page.isLast());
        return bookResponse;
    }

    @Override
    public BookResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", "category_id", categoryId));
                
        Sort sortByAndOrder = sortOrder.equals("asc") 
                ? Sort.by(sortBy).ascending()  
                : Sort.by(sortBy).descending();
        
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Book> page = bookRepository.findByCategoryOrderByPriceAsc(category, pageDetails);

        List<Book> books = page.getContent();

        if(books.isEmpty())
            throw new APIException("No books created");

        List<BookDTO> booksDTO = books.stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .toList();

        BookResponse bookResponse = new BookResponse();
        bookResponse.setContent(booksDTO);
        bookResponse.setPageNumber(page.getNumber());
        bookResponse.setPageSize(page.getSize());
        bookResponse.setTotalElements(page.getTotalElements());
        bookResponse.setTotalPages(page.getTotalPages());
        bookResponse.setLastPage(page.isLast());
        return bookResponse;
    }

    @Override
    public BookResponse searchBookByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equals("asc") 
                ? Sort.by(sortBy).ascending()  
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Book> page = bookRepository.findByNameLikeIgnoreCase('%' + keyword + '%', pageDetails);

        List<Book> books = page.getContent();

        if(books.isEmpty())
            throw new APIException("No books created");

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

        Book book = modelMapper.map(bookDTO, Book.class);

        bookDB.setName(book.getName());
        bookDB.setImage(book.getImage());
        bookDB.setPrice(book.getPrice());
        bookDB.setQuantity(book.getQuantity());
        bookDB.setCategory(book.getCategory());

        Book bookSaved = bookRepository.save(bookDB);

        List<Cart> carts = cartRepository.findCartByBookId(id);

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<BookDTO> books = cart.getCartItems().stream()
                .map(b -> modelMapper.map(b.getBook(), BookDTO.class)).collect(Collectors.toList());

                cartDTO.setBooks(books);

                return cartDTO;
        }).collect(Collectors.toList());

        cartDTOs.forEach(cart -> cartService.updateBookInCarts(cart.getId(), id));

        return modelMapper.map(bookSaved, BookDTO.class);
    }

    @Override
    public BookDTO deleteBook(Long id) {
        Book bookDB = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));

        List<Cart> carts = cartRepository.findCartByBookId(id);
        carts.forEach(cart -> cartService.deleteBookFromCart(cart.getId(), id));

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
