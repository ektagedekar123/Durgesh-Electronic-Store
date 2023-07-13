package com.lcwd.electronicstore.services;

import com.lcwd.electronicstore.payloads.AddItemToCartRequest;
import com.lcwd.electronicstore.payloads.CartDto;

public interface CartService {

    //add items to cart

    // Case 1: Cart for User is not available, we will create the cart & item to it
    // Case 2: If Cart available, add the items to cart

    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    // remove item from Cart
    void removeItemFromCart(String userId, int cartItem);

    // remove all items from Cart
    void clearCart(String userId);
}
