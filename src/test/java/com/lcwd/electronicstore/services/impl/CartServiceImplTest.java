package com.lcwd.electronicstore.services.impl;

import com.lcwd.electronicstore.entities.Cart;
import com.lcwd.electronicstore.entities.CartItem;
import com.lcwd.electronicstore.entities.Product;
import com.lcwd.electronicstore.entities.User;
import com.lcwd.electronicstore.exception.ResourceNotFoundException;
import com.lcwd.electronicstore.payloads.AddItemToCartRequest;
import com.lcwd.electronicstore.payloads.CartDto;
import com.lcwd.electronicstore.payloads.ProductDto;
import com.lcwd.electronicstore.repositories.CartItemRepository;
import com.lcwd.electronicstore.repositories.CartRepository;
import com.lcwd.electronicstore.repositories.ProductRepository;
import com.lcwd.electronicstore.repositories.UserRepository;
import com.lcwd.electronicstore.services.CartService;
import com.lcwd.electronicstore.services.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CartServiceImplTest {
    @MockBean
    private CartRepository cartRepository;
    @MockBean
    private CartItemRepository cartItemRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ModelMapper mapper;

    private User user;

    private Product product;

    private Product product1;

    private CartItem cartItem1;

    private CartItem cartItem2;

    private Cart cart;



    @BeforeEach
    public void init(){

         product= Product.builder()
                .title("Nokia X2")
                .description("This is also old version")
                .price(10000)
                .discountedPrice(1000)
                .quantity(8)
                .live(true)
                .stock(true)
                .productImage("x2.png")
                .build();

        product1= Product.builder()
                .title("Apple phone")
                .description("This is new version")
                .price(10000)
                .discountedPrice(1000)
                .quantity(8)
                .live(true)
                .stock(true)
                .productImage("apple.png")
                .build();

        user= User.builder().name("Ekta")
                .email("gedekarEkta123@gmail.com")
                .about("This is Testing method")
                .imagename("ekta.png")
                .password("lcwd")
                .gender("female")
//             .roles(Set.of(role))
                .build();

         cartItem1 = CartItem.builder()
                .product(product)
                .totalPrice(56000)
                .quantity(10)
                .build();

         cartItem2= CartItem.builder()
                .product(product1)
                .totalPrice(12000)
                .quantity(8)
                .build();

         List list=new ArrayList(List.of(cartItem1, cartItem2));
         cart = Cart.builder()
                .items(list)
                .user(user)
                .build();
        cart.setCreatedBy("Ekta");
        cart.setLastModifiedBy("Ekta");
        cart.setIsActive("Yes");



    }

    @Test
   public void addItemToCartTest() {

   //     String productId="yuu56";
        String userId="23er";

        AddItemToCartRequest cartRequest=new AddItemToCartRequest();
        cartRequest.setProductId("yuu56");
        cartRequest.setQuantity(4);

    Mockito.when(productRepository.findById(cartRequest.getProductId())).thenReturn(Optional.of(product));
    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    Mockito.when(cartRepository.save(Mockito.any())).thenReturn(cart);

        CartDto cartDto = cartService.addItemToCart(userId, cartRequest);

        Assertions.assertNotNull(cartDto);
        Assertions.assertEquals(cartDto.getUser().getName(), cart.getUser().getName());
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> cartService.addItemToCart("89", cartRequest));
   //     Assertions.assertThrows(ResourceNotFoundException.class, ()-> productRepository.findById("ty34"));
    }

    @Test
   public void removeItemFromCartTest() {

        String userId="23er";
        int cartItemId= 23;

        Mockito.when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem1));

        cartService.removeItemFromCart(userId, cartItemId);

        Mockito.verify(cartItemRepository, Mockito.times(1)).delete(cartItem1);
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> cartService.removeItemFromCart("rt45", 22));


    }

    @Test
    public void clearCartTest() {

        String userId="23er";

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart)).thenThrow(new ResourceNotFoundException("Cart is not found for user!!"));


        cartService.clearCart(userId);
        Mockito.verify(cartRepository, Mockito.times(1)).save(cart);
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> cartService.clearCart("34gh"));


    }

    @Test
    public void getCartByUserTest() {

        String userId="23er";

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart)).thenThrow(new ResourceNotFoundException("Cart for user is not found!!!"));

        CartDto cartDto = cartService.getCartByUser(userId);

        Assertions.assertNotNull(cartDto);
        Assertions.assertEquals(cart.getUser().getName(), cartDto.getUser().getName());
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> cartService.getCartByUser("erty"));

    }
}