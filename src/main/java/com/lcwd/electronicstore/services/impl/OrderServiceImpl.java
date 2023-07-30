package com.lcwd.electronicstore.services.impl;

import com.lcwd.electronicstore.entities.*;
import com.lcwd.electronicstore.exception.BadApiRequestException;
import com.lcwd.electronicstore.exception.ResourceNotFoundException;
import com.lcwd.electronicstore.helper.PageHelper;
import com.lcwd.electronicstore.payloads.CreateOrderRequest;
import com.lcwd.electronicstore.payloads.OrderDto;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.repositories.*;
import com.lcwd.electronicstore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;


    @Autowired
    private ModelMapper mapper;

    private static Logger logger=LoggerFactory.getLogger(OrderServiceImpl.class);


    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        logger.info("Initiating dao layer to create order");
        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartId));

        //fetch CartItems
        List<CartItem> cartitems = cart.getItems();
        logger.info("No. of cartItems: {}",cartitems.size());
        if(cartitems.size() <= 0){
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
        order.setCreatedBy(orderDto.getCreatedBy());
        order.setLastModifiedBy(orderDto.getLastModifiedBy());
        order.setIsActive(orderDto.getIsActive());

        // After converting into OrderItems, clear Cart & save it
        cart.getItems().clear();
        cartRepository.save(cart);

        //save order
        Order saveOrder = orderRepository.save(order);
        logger.info("Completed dao layer to create order");
        return mapper.map(saveOrder,OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        logger.info("Initiating dao layer to remove order with order id: {}",orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order is not found with id: " + orderId));
        orderRepository.delete(order);
        logger.info("Completed dao layer to remove order with order id: {}",orderId);
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        logger.info("Initiating dao layer to get order of User with user id: {}",userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found in DB!!"));

        List<Order> orderList = orderRepository.findByUser(user);

        List<OrderDto> orderDtos = orderList.stream().map(order -> mapper.map(order, OrderDto.class)).collect(Collectors.toList());
        logger.info("Completed dao layer to get order of User with user id: {}",userId);
        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNo, int pageSize, String sortBy, String sortDir) {
        logger.info("Initiating dao layer to get all orders with pageNo {}, pageSize {}, sortBy {}, sortDir {}",pageNo,pageSize,sortBy,sortDir);
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable= PageRequest.of(pageNo-1, pageSize, sort);

        Page<Order> page = orderRepository.findAll(pageable);
        logger.info("Completed dao layer to get all orders with pageNo {}, pageSize {}, sortBy {}, sortDir {}",pageNo,pageSize,sortBy,sortDir);
        return PageHelper.getPageableResponse(page, OrderDto.class);
    }

    @Override
    public OrderDto updateOrder(String orderId, OrderDto orderDto) {
        logger.info("Initiating dao layer to update order with order id: {}",orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found in DB!!"));

        order.setOrderStatus(orderDto.getOrderStatus());
        order.setDeliveredDate(new Date());
        order.setPaymentStatus(orderDto.getPaymentStatus());
        order.setIsActive(orderDto.getIsActive());

        Order updatedOrder = orderRepository.save(order);
        logger.info("Completed dao layer to update order with order id: {}",orderId);
        return mapper.map(updatedOrder, OrderDto.class);
    }
}
