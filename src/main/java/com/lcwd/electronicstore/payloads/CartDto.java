package com.lcwd.electronicstore.payloads;

import com.lcwd.electronicstore.entities.CartItem;
import com.lcwd.electronicstore.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartDto {

    private String cartId;


    private UserDto user;


    private List<CartItemDto> list=new ArrayList<>();
}
