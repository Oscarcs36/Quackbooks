package com.ds.quackbooks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ds.quackbooks.models.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.book.id = ?2")
    CartItem findCartItemByBookIdAndCartId(Long cartId, Long bookId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.book.id = ?2")
    void deleteCartItemByBookIdAndCartId(Long cartId, Long bookId);
}
