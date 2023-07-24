package com.lcwd.electronicstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronicstore.entities.Cart;
import com.lcwd.electronicstore.entities.CartItem;
import com.lcwd.electronicstore.entities.Product;
import com.lcwd.electronicstore.entities.User;
import com.lcwd.electronicstore.payloads.*;
import com.lcwd.electronicstore.services.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @MockBean
    private CartService cartService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    private CartItemDto cartItemDto1;

    private CartItemDto cartItemDto2;

    private CartDto cartDto;

    private ProductDto productDto1;

    private ProductDto productDto2;

    private UserDto userDto;

    private  String userId;

    @BeforeEach
    public void init(){

        productDto1= ProductDto.builder()
                .title("Nokia X2")
                .description("This is also old version")
                .price(10000)
                .discountedPrice(1000)
                .quantity(8)
                .live(true)
                .stock(true)
                .productImage("x2.png")
                .build();

        productDto2= ProductDto.builder()
                .title("Apple phone")
                .description("This is new version")
                .price(10000)
                .discountedPrice(1000)
                .quantity(8)
                .live(true)
                .stock(true)
                .productImage("apple.png")
                .build();

        userDto= UserDto.builder().name("Ekta")
                .email("gedekarEkta123@gmail.com")
                .about("This is Testing method")
                .imagename("ekta.png")
                .password("lcwd")
                .gender("female")
//             .roles(Set.of(role))
                .build();


        cartItemDto1 = CartItemDto.builder()
                .product(productDto1)
                .totalPrice(56000)
                .quantity(10)
                .build();

        cartItemDto2= CartItemDto.builder()
                .product(productDto2)
                .totalPrice(12000)
                .quantity(8)
                .build();

        List list=new ArrayList(List.of(cartItemDto1, cartItemDto2));
        cartDto = CartDto.builder()
                .items(list)
                .user(userDto)
                .build();
        cartDto.setCreatedBy("Ekta");
        cartDto.setLastModifiedBy("Ekta");
        cartDto.setIsActive("Yes");

        userId="ghjj45";

    }


    @Test
    public void addItemToCart() throws Exception {

      //  String userId="ghjj45";

        AddItemToCartRequest cartRequest=new AddItemToCartRequest();
        cartRequest.setProductId("yuu56");
        cartRequest.setQuantity(4);

        Mockito.when(cartService.addItemToCart(Mockito.anyString(), Mockito.any())).thenReturn(cartDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/cartapi/carts/"+userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(cartRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user").exists());



    }

    private String convertObjectToJsonString(Object cartRequest){
        try {
            return new ObjectMapper().writeValueAsString(cartRequest);
        }catch(Exception e){
            e.printStackTrace();
            return null;

        }
    }

    @Test
    public void removeItemFromCartTest() throws Exception {

    //    String userId="ghjj45";
        int cartItemId=56;

        Mockito.doNothing().when(cartService);
        cartService.removeItemFromCart(Mockito.anyString(), Mockito.anyInt());

        this.mockMvc.perform(MockMvcRequestBuilders.put("/cartapi/carts/"+userId+"/items/"+cartItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists());

    }

    @Test
    public void clearCartTest() throws Exception {
    //    String userId="ghjj45";

        Mockito.doNothing().when(cartService);
        cartService.clearCart(userId);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/cartapi/carts/"+userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists());

    }

    @Test
    public void getCartByUserTest() throws Exception {

       Mockito.when(cartService.getCartByUser(Mockito.anyString())).thenReturn(cartDto);

       this.mockMvc.perform(MockMvcRequestBuilders.get("/cartapi/carts/"+userId)
               .contentType(MediaType.APPLICATION_JSON)
               .accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.user").exists());




    }
}