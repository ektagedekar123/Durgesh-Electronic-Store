package com.lcwd.electronicstore.payloads;

import com.lcwd.electronicstore.entities.Order;
import com.lcwd.electronicstore.entities.Product;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItemsDto {

    private int orderItemId;

    private int quantity;

    private int totalPrice;

    private ProductDto product;


}
