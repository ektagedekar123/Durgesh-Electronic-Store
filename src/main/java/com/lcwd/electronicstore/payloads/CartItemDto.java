package com.lcwd.electronicstore.payloads;

import com.lcwd.electronicstore.entities.Cart;
import com.lcwd.electronicstore.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartItemDto {

    private Integer cartItemId;

    private Integer quantity;

    private Integer totalPrice;


    private ProductDto product;


}
