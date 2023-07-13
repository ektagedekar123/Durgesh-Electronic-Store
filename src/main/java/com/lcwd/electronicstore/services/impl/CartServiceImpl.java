package com.lcwd.electronicstore.services.impl;

import com.lcwd.electronicstore.entities.Product;
import com.lcwd.electronicstore.entities.User;
import com.lcwd.electronicstore.exception.ResourceNotFoundException;
import com.lcwd.electronicstore.payloads.AddItemToCartRequest;
import com.lcwd.electronicstore.payloads.CartDto;
import com.lcwd.electronicstore.repositories.CartRepository;
import com.lcwd.electronicstore.repositories.ProductRepository;
import com.lcwd.electronicstore.repositories.UserRepository;
import com.lcwd.electronicstore.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        int quantity = request.getQuantity();
        String productId = request.getProductId();

        // fetch the Product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found in DB!!"));

        // fetch the user from DB
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found in DB!!"));


        return null;
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {

    }

    @Override
    public void clearCart(String userId) {

    }
}
