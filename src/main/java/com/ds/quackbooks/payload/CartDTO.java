package com.ds.quackbooks.payload;

import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long id;
    private Double totalPrice;
    private List<BookDTO> books = new ArrayList<>();
}
