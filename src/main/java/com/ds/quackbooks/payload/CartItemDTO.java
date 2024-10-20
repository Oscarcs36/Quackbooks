package com.ds.quackbooks.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long id;

    private CartDTO cart;

    private BookDTO book;
    
    private Integer quantity;

    private Double price;
}
