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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartItemDto {

    private Integer cartItemId;

    @NotEmpty
    private Integer quantity;

    @NotBlank
    private Integer totalPrice;


    private ProductDto product;


}
