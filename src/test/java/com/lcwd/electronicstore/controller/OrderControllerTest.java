package com.lcwd.electronicstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronicstore.entities.Order;
import com.lcwd.electronicstore.payloads.CreateOrderRequest;
import com.lcwd.electronicstore.payloads.OrderDto;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
    @MockBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    private OrderDto orderDto;

    private OrderDto orderDto1;
    @BeforeEach
    public void init(){

        orderDto= OrderDto.builder()
                .billingAddress("Pratap Nagar, Nagpur")
                .billingName("Miransh Manoj Mankar")
                .billingPhone("7972565299")
                .deliveredDate(null)
                .paymentStatus("Not Paid")
                .orderStatus("Pending")
      //          .user(user)
                .build();

        orderDto.setOrderAmount(560000+8*12000);
        orderDto.setCreatedBy("Miransh");
        orderDto.setLastModifiedBy("Miransh");
        orderDto.setIsActive("Yes");

        orderDto1= OrderDto.builder()
                .billingAddress("Kalyani Nagar, Pune")
                .billingName("Ekta Manoj Mankar")
                .billingPhone("9970306253")
                .deliveredDate(null)
                .paymentStatus("Not Paid")
                .orderStatus("Pending")
                .build();
    }
    @Test
    public void createOrder() throws Exception {

        CreateOrderRequest orderRequest = CreateOrderRequest.builder()
                .userId("ghj456")
                .cartId("ser345")
                .billingAddress("Pratap Nagar, Nagpur")
                .billingName("Miransh Manoj Mankar")
                .billingPhone("7972565299")
                .build();

        orderRequest.setCreatedBy("Miransh");
        orderRequest.setLastModifiedBy("Miransh");
        orderRequest.setIsActive("Yes");

        Mockito.when(orderService.createOrder(Mockito.any())).thenReturn(orderDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/orderapi/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(orderRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.billingName").exists());
    }

    private String convertObjectToJsonString(Object orderRequest){
        try {
            return new ObjectMapper().writeValueAsString(orderRequest);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Test
    public void removeOrderTest() throws Exception {

        String orderId= "tuu67";

        Mockito.doNothing().when(orderService).removeOrder(Mockito.anyString());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/orderapi/orders/"+orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    public void getOrdersOfUserTest() throws Exception {

        String userId="uiio567";

        Mockito.when(orderService.getOrdersOfUser(Mockito.anyString())).thenReturn(List.of(orderDto, orderDto1));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/orderapi/orders/"+userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());



    }

    @Test
    public void getOrdersTest() throws Exception {

        List<Object> orderDtos = Arrays.asList(orderDto, orderDto1);

        PageableResponse response = PageableResponse.builder()
                .pageNo(1)
                .pageSize(2)
                .totalElements(50)
                .totalPages(100)
                .content(orderDtos)
                .lastPage(true)
                .build();

        Mockito.when(orderService.getOrders(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(response);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/orderapi/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }
}