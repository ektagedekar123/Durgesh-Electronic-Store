package com.lcwd.electronicstore.services.impl;

import com.lcwd.electronicstore.entities.Cart;
import com.lcwd.electronicstore.entities.CartItem;
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

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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

        Cart cart=null;
        try {
            cart = cartRepository.findByUser(user).get();
        }catch(NoSuchElementException e){
            cart=new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());

        }

        // perform Cart operation
        // If CartItem already present, then update Cart
         AtomicReference<Boolean> updated= new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        List<CartItem> updatedItems = items.stream().map(item -> {

            if (item.getProduct().getProductid().equals(productId)) {
                // Item already present in the Cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

        cart.setItems(updatedItems);

        // create item
        if(!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getPrice())
                    .cart(cart)
                    .product(product)
                    .build();

            cart.getItems().add(cartItem);
        }
        cart.setUser(user);

        Cart updatedCart = cartRepository.save(cart);

        return this.modelMapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {

    }

    @Override
    public void clearCart(String userId) {

    }
}
