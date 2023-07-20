package com.lcwd.electronicstore.services;

import com.lcwd.electronicstore.entities.Order;
import com.lcwd.electronicstore.payloads.CreateOrderRequest;
import com.lcwd.electronicstore.payloads.OrderDto;

import java.util.List;

public interface OrderService {

    //create Order
    OrderDto createOrder(CreateOrderRequest orderRequest);

    //remove Order
    void removeOrder(String userId, String orderId);

    //get Orders of User
    List<Order> getOrdersOfUser(String userId);

    //get Orders

    List<Order> getOrders();
}
