package com.ds.quackbooks.services;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ds.quackbooks.exceptions.APIException;
import com.ds.quackbooks.exceptions.ResourceNotFoundException;
import com.ds.quackbooks.models.Book;
import com.ds.quackbooks.models.Cart;
import com.ds.quackbooks.models.CartItem;
import com.ds.quackbooks.payload.BookDTO;
import com.ds.quackbooks.payload.CartDTO;
import com.ds.quackbooks.repositories.BookRepository;
import com.ds.quackbooks.repositories.CartItemRepository;
import com.ds.quackbooks.repositories.CartRepository;
import com.ds.quackbooks.util.AuthUtil;

import jakarta.transaction.Transactional;

@Service
public class CartServiceJPA implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDTO addBookToCart(Long bookId, Integer quantity) {
        Cart cart = createCart();

        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Book", "bookId", bookId));
        
        CartItem cartItem = cartItemRepository.findCartItemByBookIdAndCartId(cart.getId(), bookId);

        if(cartItem != null)
            throw new APIException("Book " + book.getName() + " already exists in the cart");

        if(book.getQuantity() == 0){
            throw new APIException(book.getName() + " is not available");
        }

        if(book.getQuantity() < quantity){
            throw new APIException("Please, make an order of the " + book.getName() + " less than or equal to the quantity");
        }

        CartItem newCartItem = new CartItem();

        newCartItem.setBook(book);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setBookPrice(book.getPrice());

        cartItemRepository.save(newCartItem);

        book.setQuantity(book.getQuantity());

        cart.setTotalPrice(cart.getTotalPrice() + (book.getPrice() * quantity));

        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();

        Stream<BookDTO> bookStream = cartItems.stream().map(item -> {
            BookDTO map = modelMapper.map(item.getBook(), BookDTO.class);
            map.setQuantity(item.getQuantity());

            return map;
        });

        cartDTO.setBooks(bookStream.toList());

        return cartDTO;
    }

    private Cart createCart(){
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());

        if(userCart != null) 
            return userCart;

        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUser());
        
        return cartRepository.save(cart);
    }

    @Override
    public List<CartDTO> getAllCarts(){
        List<Cart> carts = cartRepository.findAll();

        if(carts.size() == 0){
            throw new APIException("No cart exists");
        }

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<BookDTO> books = cart.getCartItems().stream()
                .map(b -> modelMapper.map(b.getBook(), BookDTO.class))
                .collect(Collectors.toList());
            
            cartDTO.setBooks(books);

            return cartDTO;
        }).collect(Collectors.toList());

        return cartDTOs;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);

        if(cart == null)
            throw new ResourceNotFoundException("Cart", "id", cartId);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        cart.getCartItems().forEach(c -> c.getBook().setQuantity(c.getQuantity()));
        
        List<BookDTO> books = cart.getCartItems().stream()
            .map(b -> modelMapper.map(b.getBook(), BookDTO.class))
            .collect(Collectors.toList());

        cartDTO.setBooks(books);
        return cartDTO;
    }

    @Transactional
    @Override
    public CartDTO updateBookQuantityInCart(Long bookId, Integer quantity) {
        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);
        Long cartId = userCart.getId();

        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

        if(book.getQuantity() == 0) 
            throw new APIException(book.getName() + " is not available");
        
        if(book.getQuantity() < quantity)
            throw new APIException("Please, make an order less than or equal to the quantity");
        
        CartItem cartItem = cartItemRepository.findCartItemByBookIdAndCartId(cartId, bookId);

        if(cartItem == null)
            throw new APIException(book.getName() + " not avaiable in the cart");

        int newQuantity = cartItem.getQuantity() + quantity;

        if(newQuantity < 0)
            throw new APIException("Quantity cannot be negative");

        if(newQuantity == 0){
            deleteBookFromCart(cartId, bookId);
        }else {
            cartItem.setBookPrice(book.getPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getBookPrice() * quantity));
            cartRepository.save(cart);
        }

        CartItem updatedItem = cartItemRepository.save(cartItem);

        if(updatedItem.getQuantity() == 0){
            cartItemRepository.deleteById(updatedItem.getId());
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<BookDTO> bookStream = cartItems.stream().map(item -> {
            BookDTO bk = modelMapper.map(item.getBook(), BookDTO.class);
            bk.setQuantity(item.getQuantity());
            return bk;
        });

        cartDTO.setBooks(bookStream.toList());

        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteBookFromCart(Long cartId, Long bookId) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));
        
        CartItem cartItem = cartItemRepository.findCartItemByBookIdAndCartId(cartId, bookId);

        if(cartItem == null)
            throw new ResourceNotFoundException("Book", "id", bookId);
        
        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getBookPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteCartItemByBookIdAndCartId(cartId, bookId);

        return cartItem.getBook().getName() + " removed from the cart"; 
    }

    @Override
    public void updateBookInCarts(Long cartId, Long bookId) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));

        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

        CartItem cartItem = cartItemRepository.findCartItemByBookIdAndCartId(cartId, bookId);

        if(cartItem == null)
            throw new APIException(book.getName() + " not available in the cart");

        double cartPrice = cart.getTotalPrice() - (cartItem.getBookPrice() * cartItem.getQuantity());

        cartItem.setBookPrice(book.getPrice());
        
        cart.setTotalPrice(cartPrice + (cartItem.getBookPrice() * cartItem.getQuantity()));

        cartItem = cartItemRepository.save(cartItem);
    }
}
