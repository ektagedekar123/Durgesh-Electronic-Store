package com.lcwd.electronicstore.payloads;

import com.lcwd.electronicstore.entities.CartItem;
import com.lcwd.electronicstore.entities.User;
import lombok.*;


import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CartDto extends BaseEntityDto{

    private String cartId;


    private UserDto user;


    private List<CartItemDto> items=new ArrayList<>();
}
