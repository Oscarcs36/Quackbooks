package com.ds.quackbooks.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long orderItemId;
    private BookDTO bookDTO;
    private Integer quantity;
    private double discount;
    private double orderedProductPrice;
}
