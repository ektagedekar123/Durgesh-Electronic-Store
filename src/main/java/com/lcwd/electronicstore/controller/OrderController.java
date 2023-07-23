package com.lcwd.electronicstore.controller;

import com.lcwd.electronicstore.helper.AppConstants;
import com.lcwd.electronicstore.payloads.ApiResponse;
import com.lcwd.electronicstore.payloads.CreateOrderRequest;
import com.lcwd.electronicstore.payloads.OrderDto;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orderapi")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest orderRequest){

        OrderDto orderDto = orderService.createOrder(orderRequest);

        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse> removeOrder(@PathVariable String orderId){

        orderService.removeOrder(orderId);

        ApiResponse apiResponse = ApiResponse.builder().status(HttpStatus.OK).success(true).message("Order is removed!!").build();

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/orders/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId){

        List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);

        return new ResponseEntity<>(ordersOfUser, HttpStatus.OK);

    }

    @GetMapping("/orders")
    public ResponseEntity<PageableResponse<OrderDto>> getOrders(
            @RequestParam(value="pageNo", defaultValue = AppConstants.PAGE_NO, required = false) int pageNo,
            @RequestParam(value="pageSize", defaultValue= AppConstants.PAGE_SIZE, required= false) int pageSize,
            @RequestParam(value="sortBy", defaultValue = AppConstants.SORT_BYOrderedDate, required= false) String sortBy,
            @RequestParam(value="sortDir", defaultValue= AppConstants.SORT_DIR, required=false) String sortDir){

        PageableResponse<OrderDto> pageableResponse = orderService.getOrders(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }
}
