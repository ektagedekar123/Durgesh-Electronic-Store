package com.lcwd.electronicstore.controller;

import com.lcwd.electronicstore.payloads.AddItemToCartRequest;
import com.lcwd.electronicstore.payloads.ApiResponse;
import com.lcwd.electronicstore.payloads.CartDto;
import com.lcwd.electronicstore.services.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartapi")
public class CartController {

    @Autowired
    private CartService cartService;

    private static Logger logger= LoggerFactory.getLogger(CartController.class);

    @PostMapping("/carts/{userid}")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody AddItemToCartRequest request, @PathVariable("userid") String userId) {
        logger.info("Entering request for adding Cart item in Cart with userId {}",userId);
        CartDto cartDto = cartService.addItemToCart(userId, request);
        logger.info("Completed request for adding Cart item in Cart with userId {}",userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @PutMapping("/carts/{userid}/items/{cartitemid}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable("userid")  String userId, @PathVariable("cartitemid") int cartItemId){

        cartService.removeItemFromCart(userId, cartItemId);

        ApiResponse response = ApiResponse.builder()
                .message("Item is removed from cart !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @DeleteMapping("/carts/{userId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable String userId){

        cartService.clearCart(userId);

        ApiResponse response = ApiResponse.builder()
                .message("All items are removed from cart & Not Card is Blank !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/carts/{userId}")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable String userId){
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);

    }
}
