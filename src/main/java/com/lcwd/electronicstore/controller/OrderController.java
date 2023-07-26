package com.lcwd.electronicstore.controller;

import com.lcwd.electronicstore.helper.AppConstants;
import com.lcwd.electronicstore.payloads.ApiResponse;
import com.lcwd.electronicstore.payloads.CreateOrderRequest;
import com.lcwd.electronicstore.payloads.OrderDto;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orderapi")
public class OrderController {

    private static Logger logger= LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest orderRequest){
        logger.info("Entering request for creating logger");
        OrderDto orderDto = orderService.createOrder(orderRequest);
        logger.info("Completed request for creating logger");
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse> removeOrder(@PathVariable String orderId){
        logger.info("Entering request for removing order with orderId: {}",orderId);
        orderService.removeOrder(orderId);

        ApiResponse apiResponse = ApiResponse.builder().status(HttpStatus.OK).success(true).message("Order is removed!!").build();
        logger.info("Completed request for removing order with orderId: {}",orderId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/orders/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId){
        logger.info("Entering request to get order of user with user id: {}",userId);
        List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
        logger.info("Completed request to get order of user with user id: {}",userId);
        return new ResponseEntity<>(ordersOfUser, HttpStatus.OK);

    }

    @GetMapping("/orders")
    public ResponseEntity<PageableResponse<OrderDto>> getOrders(
            @RequestParam(value="pageNo", defaultValue = AppConstants.PAGE_NO, required = false) int pageNo,
            @RequestParam(value="pageSize", defaultValue= AppConstants.PAGE_SIZE, required= false) int pageSize,
            @RequestParam(value="sortBy", defaultValue = AppConstants.SORT_BYOrderedDate, required= false) String sortBy,
            @RequestParam(value="sortDir", defaultValue= AppConstants.SORT_DIR, required=false) String sortDir){
        logger.info("Entering request to get All orders with pageNO {}, pageSize {}, sortBy {}, sortDir {}",pageNo,pageSize,sortBy,sortDir);
        PageableResponse<OrderDto> pageableResponse = orderService.getOrders(pageNo, pageSize, sortBy, sortDir);
        logger.info("Completed request to get All orders with pageNO {}, pageSize {}, sortBy {}, sortDir {}",pageNo,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }
}
