package com.ds.quackbooks.services;

import com.ds.quackbooks.payload.CartDTO;

import java.util.*;

public interface CartService {
    CartDTO addBookToCart(Long bookId, Integer quantity);
    List<CartDTO> getAllCarts();
    CartDTO getCart(String emailId, Long cartId);
    CartDTO updateBookQuantityInCart(Long bookId, Integer quantity);
    String deleteBookFromCart(Long cartId, Long bookId);
    void updateBookInCarts(Long cartId, Long bookId);
}
