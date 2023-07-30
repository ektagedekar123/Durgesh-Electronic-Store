package com.lcwd.electronicstore.services;

import com.lcwd.electronicstore.entities.Order;
import com.lcwd.electronicstore.payloads.CreateOrderRequest;
import com.lcwd.electronicstore.payloads.OrderDto;
import com.lcwd.electronicstore.payloads.PageableResponse;

import java.util.List;

public interface OrderService {

    //create Order
    OrderDto createOrder(CreateOrderRequest orderRequest);

    //remove Order
    void removeOrder(String orderId);

    //get Orders of User
    List<OrderDto> getOrdersOfUser(String userId);

    //get Orders

    PageableResponse<OrderDto> getOrders(int pageNo, int pageSize, String sortBy, String sortDir);

    //update Order
    OrderDto updateOrder(String orderId, OrderDto orderDto);
}
