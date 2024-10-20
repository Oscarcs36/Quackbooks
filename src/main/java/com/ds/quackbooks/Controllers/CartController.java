package com.ds.quackbooks.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ds.quackbooks.models.Cart;
import com.ds.quackbooks.payload.CartDTO;
import com.ds.quackbooks.repositories.CartRepository;
import com.ds.quackbooks.services.CartService;
import com.ds.quackbooks.util.AuthUtil;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired 
    AuthUtil authUtil;

    @PostMapping("/carts/book/{bookId}/quantity/{quantity}")
    public ResponseEntity<?> addBookToCart(@PathVariable Long bookId, @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addBookToCart(bookId, quantity);

        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<?> getCarts() {
        List<CartDTO> cartDTOs = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOs, HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<?> getCartById() {
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);
        Long cartId = cart.getId();

        CartDTO cartDTO = cartService.getCart(emailId, cartId);

        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }
    
    @PutMapping("/carts/books/{bookId}/quantity/{operation}")
    public ResponseEntity<?> updateCartProduct(@PathVariable Long bookId, @PathVariable String operation){
        CartDTO cartDTO = cartService.updateBookQuantityInCart(bookId, operation.equalsIgnoreCase("delete") ? -1 : 1);

        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/carts/{cartId}/book/{bookId}")
    public ResponseEntity<?> deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long bookId){
        String status = cartService.deleteBookFromCart(cartId, bookId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
