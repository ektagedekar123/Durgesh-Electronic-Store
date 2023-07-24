package com.lcwd.electronicstore.payloads;

import com.lcwd.electronicstore.entities.Cart;
import com.lcwd.electronicstore.entities.Product;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CartItemDto {

    private Integer cartItemId;

    @NotEmpty
    private Integer quantity;

    @NotBlank
    private Integer totalPrice;


    private ProductDto product;


}
