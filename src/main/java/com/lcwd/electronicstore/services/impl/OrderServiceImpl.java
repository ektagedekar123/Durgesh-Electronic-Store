package com.lcwd.electronicstore.services.impl;

import com.lcwd.electronicstore.entities.*;
import com.lcwd.electronicstore.exception.BadApiRequestException;
import com.lcwd.electronicstore.exception.ResourceNotFoundException;
import com.lcwd.electronicstore.payloads.CreateOrderRequest;
import com.lcwd.electronicstore.payloads.OrderDto;
import com.lcwd.electronicstore.repositories.*;
import com.lcwd.electronicstore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper mapper;


    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {

        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartId));

        //fetch CartItems
        List<CartItem> cartitems = cart.getItems();

        if(cartitems.size()<=0){
             throw new BadApiRequestException("Invalid no. of items in Cart!!");
        }

        // Generate Order
        Order order = Order.builder()
                .OrderId(UUID.randomUUID().toString())
                .billingAddress(orderDto.getBillingAddress())
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .user(user)
                .build();

        // Convert CartItems to OrderItems

        AtomicReference<Integer> orderAmount=new  AtomicReference<Integer>(0);
        List<OrderItems> orderItems = cartitems.stream().map(cartitem -> {

            OrderItems orderItem = OrderItems.builder()
                    .quantity(cartitem.getQuantity())
                    .product(cartitem.getProduct())
                    .totalPrice(cartitem.getQuantity() * cartitem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();
            orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        // After converting into OrderItems, clear Cart & save it
        cart.getItems().clear();
        cartRepository.save(cart);

        //save order
        Order saveOrder = orderRepository.save(order);

        return mapper.map(saveOrder,OrderDto.class);
    }

    @Override
    public void removeOrder(String userId, String orderId) {

    }

    @Override
    public List<Order> getOrdersOfUser(String userId) {
        return null;
    }

    @Override
    public List<Order> getOrders() {
        return null;
    }
}
