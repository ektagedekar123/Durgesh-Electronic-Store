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

    /**
     * @Author Ekta
     * @apiNote This method is for adding Cart item in Cart
     * @param request
     * @param userId
     * @return CartDto
     */
    @PostMapping("/carts/{userid}")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody AddItemToCartRequest request, @PathVariable("userid") String userId) {
        logger.info("Entering request for adding Cart item in Cart with userId {}",userId);
        CartDto cartDto = cartService.addItemToCart(userId, request);
        logger.info("Completed request for adding Cart item in Cart with userId {}",userId);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }


    /**
     * @Author Ekta
     * @apiNote This method is for removing Cart item from Cart
     * @param userId
     * @param cartItemId
     * @return ApiResponse
     */
    @PutMapping("/carts/{userid}/items/{cartitemid}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable("userid")  String userId, @PathVariable("cartitemid") int cartItemId){
        logger.info("Entering request to remove cart item from cart with userId {} & cartItemId {}",userId, cartItemId);
        cartService.removeItemFromCart(userId, cartItemId);

        ApiResponse response = ApiResponse.builder()
                .message("Item is removed from cart !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        logger.info("Completed request to remove cart item from cart with userId {} & cartItemId {}",userId, cartItemId);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    /**
     * @Author Ekta
     * @apiNote This method is to remove all Cart items from Cart
     * @param userId
     * @return ApiResponse
     */
    @DeleteMapping("/carts/{userId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable String userId){
    logger.info("Entering request to clear all items from Cart with user id {}", userId);
        cartService.clearCart(userId);

        ApiResponse response = ApiResponse.builder()
                .message("All items are removed from cart, Now Card is Blank !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        logger.info("Completed request to clear all items from Cart with user id {}", userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * @Author Ekta
     * @apiNote This Method is for getting Cart of User
     * @param userId
     * @return CartDto
     */
    @GetMapping("/carts/{userId}")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable String userId){
        logger.info("Entering request to get Cart of User with user id {}",userId);
        CartDto cartDto = cartService.getCartByUser(userId);
        logger.info("Completed request to get Cart of User with user id {}",userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);

    }
}
