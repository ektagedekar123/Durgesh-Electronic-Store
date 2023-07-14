package com.lcwd.electronicstore.services.impl;

import com.lcwd.electronicstore.entities.Cart;
import com.lcwd.electronicstore.entities.CartItem;
import com.lcwd.electronicstore.entities.Product;
import com.lcwd.electronicstore.entities.User;
import com.lcwd.electronicstore.exception.BadApiRequestException;
import com.lcwd.electronicstore.exception.ResourceNotFoundException;
import com.lcwd.electronicstore.payloads.AddItemToCartRequest;
import com.lcwd.electronicstore.payloads.CartDto;
import com.lcwd.electronicstore.repositories.CartItemRepository;
import com.lcwd.electronicstore.repositories.CartRepository;
import com.lcwd.electronicstore.repositories.ProductRepository;
import com.lcwd.electronicstore.repositories.UserRepository;
import com.lcwd.electronicstore.services.CartService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
       log.info("Initiating dao layer for adding cart item in cart with user id {]",userId);
        int quantity = request.getQuantity();
        String productId = request.getProductId();
        log.info("fetching data from AddItemToCartRequest with quantity {} & productId {}",quantity,productId);
        if (quantity<=0)
        {
            throw new BadApiRequestException("Requested Quantity is not valid !!");
        }

        // fetch the Product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found in DB!!"));

        // fetch the user from DB
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found in DB!!"));

        Cart cart= null;
        try {
            cart = cartRepository.findByUser(user).get();
        }catch(NoSuchElementException e){
            cart=new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());

        }
        log.info("Cart is created : {}", cart);
        // perform Cart operation
        // If CartItem already present, then update Cart
         AtomicReference<Boolean> updated= new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        log.info("items in cart: {}",items);
        List<CartItem> updatedItems = items.stream().map(item -> {

            if (item.getProduct().getProductid().equals(productId)) {
                // Item already present in the Cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).toList();
        log.info("updated items: {}",updatedItems);
        cart.setItems(updatedItems);
        log.info("Cart object After adding item: {}",cart);

        // create item
        if(!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();

            cart.getItems().add(cartItem);
        }
        log.info("If item is not present in cart then item is added & Cart object is: {}",cart);
        cart.setUser(user);

        Cart updatedCart = cartRepository.save(cart);
        log.info("Completed dao layer for adding cart item in cart with user id {]",userId);
        return this.modelMapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItemId) {
        log.info("Initiating dao layer to remove cart item with user id {} & cart item id {}",userId, cartItemId);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart Item not Found in DB!!"));
        cartItemRepository.delete(cartItem);
        log.info("Completed dao layer to remove cart item with user id {} & cart item id {}",userId, cartItemId);
    }

    @Override
    public void clearCart(String userId) {
        log.info("Initiating dao layer to clear all cart items from cart with user id {}",userId);
        // fetch User from DB
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id" + userId));

        // fetch Cart of User
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given User not found !!"));

        //Remove CartItems from Cart
        cart.getItems().clear();

        cartRepository.save(cart);
        log.info("Completed dao layer to clear all cart items from cart with user id {}",userId);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        log.info("Initiating dao layer to get Cart of User with user id {}",userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not Found with " + userId));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given User not found !!"));
        log.info("Completed dao layer to get Cart of User with user id {}",userId);
        return modelMapper.map(cart, CartDto.class);
    }
}
